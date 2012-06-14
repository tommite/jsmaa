/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.smaa.jsmaa.model.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.XMLReferenceResolver;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.BetaMeasurement;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.LogitNormalMeasurement;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.RelativeLogitNormalMeasurement;
import fi.smaa.jsmaa.model.RelativeNormalMeasurement;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

@SuppressWarnings("serial")
public class JSMAABinding extends XMLBinding {

	public JSMAABinding() {
		setAliases();
	}

	public static SMAAModel readModel(InputStream is) throws XMLStreamException {
		XMLObjectReader reader = new XMLObjectReader().setInput(is).setBinding(new JSMAABinding());
		reader.setReferenceResolver(new XMLReferenceResolver());
		return reader.read();
	}
	
	public static void writeModel(SMAAModel model, OutputStream os) throws XMLStreamException {
		XMLObjectWriter writer = new XMLObjectWriter().setOutput(os).setBinding(new JSMAABinding());
		writer.setReferenceResolver(new XMLReferenceResolver());		
		writer.setIndentation("\t");
		if (model instanceof SMAATRIModel) {
			writer.write((SMAATRIModel) model, "SMAA-TRI-model", SMAATRIModel.class);
		} else {
			writer.write(model, "SMAA-2-model", SMAAModel.class);
		}
		writer.close();
	}
	
	private void setAliases() {
		setAlias(Rank.class, "rank");
		setAlias(GaussianMeasurement.class, "gaussian");
		setAlias(BetaMeasurement.class, "beta");
		setAlias(LogNormalMeasurement.class, "lognormal");
		setAlias(LogitNormalMeasurement.class, "logitNormal");
		setAlias(RelativeLogitNormalMeasurement.class, "relativeLogitNormal");
		setAlias(RelativeNormalMeasurement.class, "relativeNormal");
		setAlias(ExactMeasurement.class, "exact");
		setAlias(CriterionMeasurementPair.class, "criterionMeasurement");
		setAlias(CriterionAlternativeMeasurement.class, "criterionAlternativeMeasurement");
		setAlias(ScaleCriterion.class, "cardinalCriterion");
		setAlias(OrdinalCriterion.class, "ordinalCriterion");
		setAlias(OutrankingCriterion.class, "outrankingCriterion");
		setAlias(Alternative.class, "alternative");
		setAlias(CardinalPreferenceInformation.class, "cardinalPreferences");
		setAlias(OrdinalPreferenceInformation.class, "ordinalPreferences");
		setAlias(Interval.class, "interval");
		setAlias(SMAAModel.class, "SMAA-2-model");
		setAlias(SMAATRIModel.class, "SMAA-TRI-model");
	}		
}
