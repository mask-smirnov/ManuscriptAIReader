package io.github;

import java.awt.image.BufferedImage;

import lombok.Getter;

/**
 * This class determines paper color by analyzing histogram.
 * Paper color is a range of colors that covers significant part of image area.
 * We only analyze center of the image, because on the sides we can meet artifacts like table, shadow of
 * the sheet, fingers etc. This margins cropping is configured by CROPPING_MARGINS parameter.
 */
public class PaperColorFinder
{
    private final BufferedImage image;
    private Histogram histogram;

    public static final double CROPPING_MARGINS = 10; //Margins, pct. Large margins a recommended to cut off possible objects outside the manuscript

    public final double INK_PAPER_THRESHOLD_FOR_APPR_SEARCH = 5; // The luminance drop on a histogram has to be so many times
                                                                 // to approximately determine that this is not a paper color

    public final double INK_PAPER_THRESHOLD = 5; // The luminance drop on a histogram has to be so many times
                                                 // to determine that this is not a paper color

    @Getter
    private int mostFrequentLuminance = 0; //paper color peak

    @Getter
    private int paperInkBorderApprox;
    @Getter
    private int paperColorLighterBorderApprox;

    @Getter
    private int paperInkBorder;

    public PaperColorFinder(BufferedImage image) throws ImageIsNotAManuscriptException
    {
        this.image = image;

        run();
    }

    private void run() throws ImageIsNotAManuscriptException
    {
        histogram = new Histogram(image, CROPPING_MARGINS);

        searchForPaperColorMaximum();

        paperInkBorderApprox = searchForApproximatePaperInkBorder();
        System.out.printf("paperInkBorderApprox %d\n", paperInkBorderApprox);

        paperColorLighterBorderApprox = searchForApproximatePaperColorLighterBorder();
        System.out.printf("paperColorLighterBorderApprox %d\n", paperColorLighterBorderApprox);

        // For the future:
        // paperInkBorderApprox and paperColorLighterBorderApprox are not true boundaries of paper color
        // They are calculated using arbitrary chosen threshold
        // To determine true paper color we take rectangular probes of areas that doesn't contain many
        // pixels of colors outside these boundaries. Two scenarios are possible:
        // 1. Underestimate (paperInkBorderApprox and paperColorLighterBorderApprox are narrower than actual
        //    boundaries). In this case we will not be able to find big rectangles. So we need to make luminance
        //    boundaries wider until rectangles will be big enough.
        // 2. Overestimate (they are wider). In this case luminance distribution around paper color will be
        //    the same as in overall image. So we need to make boundaries narrower until luminance distribution
        //    will become different.

        // This is not done now. In case there will be problems in determining paper-ink border, do this.

        //Searches for pieces of paper without ink
        ColorProbesMonteCarlo probes = new ColorProbesMonteCarlo(
            image,
            paperInkBorderApprox,
            paperColorLighterBorderApprox,
            false
        );

        if (probes.isSuccess())
        {
            Histogram histogram = new Histogram(image, probes.getSuccesfullProbes());
            int paperOnlyMaximum = histogram.searchForPaperColorMaximum();

            paperInkBorder = histogram.searchForLuminanceTreshold(paperOnlyMaximum - 1, -1, INK_PAPER_THRESHOLD);

            return;
        }

        throw new ImageIsNotAManuscriptException("Cannot determine paper-ink luminance border");
    }

    //determines paper-ink luminance border approximately by meeting threshold
    private int searchForApproximatePaperInkBorder() throws ImageIsNotAManuscriptException
    {
        return histogram.searchForLuminanceTreshold(mostFrequentLuminance - 1, -1, INK_PAPER_THRESHOLD_FOR_APPR_SEARCH);
    }

    //determines paper color lighter luminance border approximately by meeting threshold
    private int searchForApproximatePaperColorLighterBorder()
    {
        if (mostFrequentLuminance == ColorModel.COLORS_PER_CHANNEL - 1)
            return mostFrequentLuminance;

        return histogram.searchForLuminanceTreshold(mostFrequentLuminance + 1, 1, INK_PAPER_THRESHOLD_FOR_APPR_SEARCH);
    }

    //Searches for a maximum on a histogram taking into account neighbours of each point (+|- PROXIMITY_FOR_MAXIMUM_SEARCH)
    private void searchForPaperColorMaximum()
    {
        mostFrequentLuminance = histogram.searchForPaperColorMaximum();
    }
}
