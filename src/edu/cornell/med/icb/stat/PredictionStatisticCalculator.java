package edu.cornell.med.icb.stat;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.doubles.DoubleArraySet;

/**
 * @author Fabien Campagne
 *         Date: Oct 8, 2009
 *         Time: 5:22:59 PM
 */
public abstract class PredictionStatisticCalculator {
    public double optimalThreshold;
    protected double statistic;
    protected boolean highestStatisticIsBest = false;
    public abstract String getMeasureName();

    /**
     * Calculates the optimal statistic at any decision threshold. All the possible thresholds on the decision value
     * are scanned and the optimal statistic found is returned.
     * When highestStatisticIsBest is true, the largest statistic found at any threshold is returned. Otherwise,
     * the lowest statistic is returned.
     *
     * @param decisionValueList Each element of this list should corresponds to a split of evaluation (decision values).
     * @param labelList         Each element of this list should corresponds to a split of evaluation (true labels).
     * @return
     */
    public double thresholdIndependentStatistic(final ObjectList<double[]> decisionValueList, final ObjectList<double[]> labelList) {

        // any unique decision value is a potential decision threshold:
        final DoubleSet thresholds = new DoubleArraySet();
        for (final double[] decisionValues : decisionValueList) {
            for (final double value : decisionValues) {
                thresholds.add(value);
            }
        }
        double selectedThreshold = -1;
        double optimalStatistic = highestStatisticIsBest ? Double.MIN_VALUE : Double.MAX_VALUE;
        for (final double threshold : thresholds) {

            final double statisticsValueAtThreshold = evaluateStatisticAtThreshold(threshold, decisionValueList, labelList);
            if (highestStatisticIsBest && statisticsValueAtThreshold > optimalStatistic) {
                optimalStatistic = statisticsValueAtThreshold;
                selectedThreshold = threshold;
            } else {
                if (!highestStatisticIsBest && statisticsValueAtThreshold < optimalStatistic) {
                    optimalStatistic = statisticsValueAtThreshold;
                    selectedThreshold = threshold;
                }
            }
        }
        statistic = optimalStatistic;
        optimalThreshold = selectedThreshold;
        return statistic;
    }

    /**
     * Calculates the Matthews Correlation coefficient. Find the maximal MCC value irrespective of
     * the threshold on the decision value. All the possible thresholds on the decision value
     * are scanned and the maximum MCC values found is returned.
     *
     * @param decisionValues
     * @param labels
     * @return
     */
    public double thresholdIndependentStatistic(final double[] decisionValues, final double[] labels) {
        // any unique decision value is a potential decision threshold:
        final DoubleSet thresholds = new DoubleArraySet();
        for (final double value : decisionValues) {
            thresholds.add(value);
        }

        double selectedThreshold = -1;
        double optimalStatistic = highestStatisticIsBest ? Double.MIN_VALUE : Double.MAX_VALUE;
        for (final double threshold : thresholds) {

            final double statisticsValueAtThreshold = evaluateStatisticAtThreshold(threshold, decisionValues, labels);
            if (highestStatisticIsBest && statisticsValueAtThreshold >= optimalStatistic) {
                optimalStatistic = statisticsValueAtThreshold;
                selectedThreshold = threshold;
            } else {
                if (!highestStatisticIsBest && statisticsValueAtThreshold <= optimalStatistic) {
                    optimalStatistic = statisticsValueAtThreshold;
                    selectedThreshold = threshold;
                }
            }
        }
        statistic = optimalStatistic;
        optimalThreshold = selectedThreshold;
        return statistic;
    }

    public double thresholdIndependentStatisticStd(final ObjectList<double[]> decisionValueList, final ObjectList<double[]> trueLabelList) {
        final ZScoreCalculator calc = new ZScoreCalculator();

        for (int i = 0; i < decisionValueList.size(); i++) {
            final double mccSingleSplit = evaluateStatisticAtThreshold(optimalThreshold, decisionValueList.get(i), trueLabelList.get(i));
            calc.observe(mccSingleSplit);
        }
        calc.calculateStats();
        return calc.stdDev();
    }
      /**
     * Calculate the standard error of the mean of the statistic.
     * Return Std of the statistic divided by the square root of the number of observations.
     *
     * @param decisionValueList
     * @param trueLabelList
     * @return
     */

    public double thresholdIndependentStatisticSte(final ObjectList<double[]> decisionValueList, final ObjectList<double[]> trueLabelList) {

        return thresholdIndependentStatisticStd(decisionValueList, trueLabelList) / Math.sqrt(decisionValueList.size());
    }
    public double evaluateStatisticAtThreshold(final double threshold, final ObjectList<double[]> decisionValueList, final ObjectList<double[]> labelList) {
        double averageStatistic = 0;
        double count = 0;
        for (int i = 0; i < decisionValueList.size(); i++) {
            final double statisticSingleSplit = evaluateStatisticAtThreshold(threshold, decisionValueList.get(i), labelList.get(i));
            averageStatistic += statisticSingleSplit;
            count++;
        }
        return averageStatistic / count;
    }

    /**
     * Evaluate the statistic for a given decision function threshold.
     *
     * @param threshold
     * @param decisionValues
     * @param labels
     */
    public abstract double evaluateStatisticAtThreshold(final double threshold, final double[] decisionValues, final double[] labels);

}
