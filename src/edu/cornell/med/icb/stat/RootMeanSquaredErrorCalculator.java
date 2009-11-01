package edu.cornell.med.icb.stat;

import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Calculates the root mean squared error (RMSE). This measure is independent of any threshold. The RMSE should be used
 * only for regression, when the decision value is expected to be on the same scale as the label.
 * <p/>
 * See definition at http://cran.r-project.org/web/packages/ROCR/ROCR.pdf
 *
 * @author Fabien Campagne
 *         Date: Oct 9, 2009
 *         Time: 3:44:20 PM
 */
public class RootMeanSquaredErrorCalculator extends PredictionStatisticCalculator {
    public String getMeasureName() {
        return "RMSE";
    }

    public RootMeanSquaredErrorCalculator() {
        highestStatisticIsBest = true;
        zero = 0;
    }

    public double evaluateStatisticAtThreshold(double threshold, double[] decisionValues, double[] labels) {

        double meanSquareError = 0;
        for (int i = 0; i < decisionValues.length; i++) {
            final double error = decisionValues[i] - labels[i];
            meanSquareError += error * error;

        }
        meanSquareError /= (double) decisionValues.length;
        return Math.sqrt(meanSquareError);

    }

    public double thresholdIndependentStatistic(final double[] decisionValues, final double[] labels) {
        return evaluateStatisticAtThreshold(0, decisionValues, labels);
    }

    public double thresholdIndependentStatistic(final ObjectList<double[]> decisionValueList, final ObjectList<double[]> labelList) {
        double averageStatistic = 0;
        double count = 0;
        for (int i = 0; i < decisionValueList.size(); i++) {
            final double statisticSingleSplit = thresholdIndependentStatistic(decisionValueList.get(i), labelList.get(i));
            averageStatistic += statisticSingleSplit;
            count++;
        }
        return averageStatistic / count;
    }

}
