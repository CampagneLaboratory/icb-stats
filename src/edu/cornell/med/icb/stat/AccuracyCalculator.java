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
 * Calculates the accuracy of predictions.
 *
 * @author Fabien Campagne
 *         Date: Apr 23, 2008
 *         Time: 3:23:25 PM
 */
public class AccuracyCalculator extends PredictionStatisticCalculator {
    public String getMeasureName() {
        return "Accuracy";
    }

    public AccuracyCalculator() {
        highestStatisticIsBest = true;
    }



    /**
     * Evaluate the accuracy for a given decision function threshold.
     *
     * @param threshold
     * @param decisionValues
     * @param labels
     * @return Accuracy of the prediction at threshold.
     */
    public double evaluateAccuracy(final double threshold, final double[] decisionValues, final double[] labels) {
        evaluateContingencyTable(threshold, decisionValues, labels);

        double value = (TP + TN) /
                (TP + TN + FN + FP);
        if (value != value) {
            // NaN
            value = 0;
        }
        return value;
    }


    public double evaluateStatisticAtThreshold(double threshold, double[] decisionValues, double[] labels) {
        return evaluateAccuracy(threshold, decisionValues, labels);
    }
}