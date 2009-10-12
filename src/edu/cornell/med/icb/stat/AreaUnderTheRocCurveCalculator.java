package edu.cornell.med.icb.stat;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Fabien Campagne
 *         Date: Oct 8, 2009
 *         Time: 5:42:26 PM
 */
public class AreaUnderTheRocCurveCalculator extends PredictionStatisticCalculator {
    public String getMeasureName() {
        return "auc";
    }

    /**
     * Used to log debug and informational messages.
     */
    private static final Log LOG = LogFactory.getLog(AreaUnderTheRocCurveCalculator.class);

    public AreaUnderTheRocCurveCalculator() {
        highestStatisticIsBest = true;
        zero=0.5;
    }

    public double evaluateStatisticAtThreshold(double threshold, double[] decisionValues, double[] labels) {
        return Double.NaN;
    }

    public double thresholdIndependentStatistic(final double[] decisionValues, final double[] labels) {
        return evaluateStatistic(decisionValues, labels);
    }

    public double evaluateStatistic(double[] decisionValues, double[] labels) {
        double sum = 0;
        double numPositive = 0;
        double numNegative = 0;

        DoubleList truePositiveDecisions = new DoubleArrayList();
        DoubleList trueNegativeDecisions = new DoubleArrayList();
        for (int i = 0; i < decisionValues.length; i++) {
            if (decisionValues[i] != decisionValues[i]) {
                // decision value is NaN:
                LOG.warn("NaN found instead of a decision value. NaN are always interpreted as wrong predictions. ");
            }
            if (labels[i] >= 0) truePositiveDecisions.add(decisionValues[i]);
            else {
                trueNegativeDecisions.add(decisionValues[i]);
            }
        }

        for (double decisionPositive : truePositiveDecisions) {


            for (double decisionNegative : trueNegativeDecisions) {
                sum += decisionPositive > decisionNegative ? 1 : 0;
                sum += decisionPositive == decisionNegative ? 0.5 : 0;

            }

        }

        numPositive = truePositiveDecisions.size();
        numNegative = trueNegativeDecisions.size();

        double auc = sum / numPositive / numNegative;
        return auc;
    }


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
        double averageStatistic = 0;
        double count = 0;
        for (int i = 0; i < decisionValueList.size(); i++) {
            final double statisticSingleSplit = thresholdIndependentStatistic(decisionValueList.get(i), labelList.get(i));
            averageStatistic += statisticSingleSplit;
            count++;
        }
        return averageStatistic / count;
    }

    public double thresholdIndependentStatisticStd(final ObjectList<double[]> decisionValueList, final ObjectList<double[]> trueLabelList) {
        final ZScoreCalculator calc = new ZScoreCalculator();

        for (int i = 0; i < decisionValueList.size(); i++) {
            final double aucSingleSplit = evaluateStatistic(decisionValueList.get(i), trueLabelList.get(i));
            calc.observe(aucSingleSplit);
        }
        calc.calculateStats();
        return calc.stdDev();
    }


}
