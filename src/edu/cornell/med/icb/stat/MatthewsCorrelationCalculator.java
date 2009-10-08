/*
 * Copyright (C) 2008-2009 Institute for Computational Biomedicine,
 *                         Weill Medical College of Cornell University
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.cornell.med.icb.stat;

import it.unimi.dsi.fastutil.doubles.DoubleArraySet;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Calculates the Matthews Correlation coefficient.
 *
 * @author Fabien Campagne
 *         Date: Apr 23, 2008
 *         Time: 3:23:25 PM
 */
public class MatthewsCorrelationCalculator extends PredictionStatisticCalculator {




    /**
     * Evaluate the Mathews Correlation coefficient for a given decision function threshold.
     *
     * @param threshold
     * @param decisionValues
     * @param labels
     */
    public double evaluateMCC(final double threshold, final double[] decisionValues, final double[] labels) {
        final double[] copyOfDecisionValues = new double[decisionValues.length];
        System.arraycopy(decisionValues, 0, copyOfDecisionValues, 0, decisionValues.length);
        // make decision binary according to threshold:

        for (int i = 0; i < copyOfDecisionValues.length; i++) {
            if (copyOfDecisionValues[i] < threshold) {
                copyOfDecisionValues[i] = 0;
            } else {
                copyOfDecisionValues[i] = 1;
            }

        }
        int tp = 0;
        int tn = 0;
        int fn = 0;
        int fp = 0;
        for (int i = 0; i < copyOfDecisionValues.length; i++) {
            final double binaryDecision = copyOfDecisionValues[i];
            final double trueLabel = labels[i];
            if (trueLabel == 1) {
                if (binaryDecision == 1) {
                    tp++;
                } else {
                    fn++;
                }
            } else { // True label=0
                if (binaryDecision == 0) {
                    tn++;
                } else {
                    fp++;
                }
            }
        }
        final double TP = tp;
        final double TN = tn;
        final double FN = fn;
        final double FP = fp;
        double value = (TP * TN - FP * FN) /
                Math.sqrt((TP + FP) * (TP + FN) * (TN + FP) * (TN + FN));
        if (value != value) {
            // NaN
            value = 0;
        }
        return value;
    }

    public double evaluateStatisticAtThreshold(double threshold, double[] decisionValues, double[] labels) {
        return evaluateMCC(threshold,  decisionValues, labels);
    }
}
