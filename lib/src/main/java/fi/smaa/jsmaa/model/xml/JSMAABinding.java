package fi.smaa.jsmaa.model.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
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
	
	@SuppressWarnings("unchecked")
	@Override
	protected XMLFormat getFormat(Class cls) throws XMLStreamException{
		if (String.class.equals(cls)){ 
			return stringXMLFormat;
		}
		return super.getFormat(cls);
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
	
	private XMLFormat<String> stringXMLFormat = new XMLFormat<String>(null) {
		@Override
		public boolean isReferenceable() {
			return false;
		}		
		@Override
		public void write(String obj, OutputElement xml) throws XMLStreamException {
			xml.addText(obj);
		}
		@Override
		public void read(InputElement xml, String obj) throws XMLStreamException {
		}
		@Override
		public String newInstance(Class<String> cls, InputElement ie) throws XMLStreamException {
			return ie.getText().toString();
		}		
	};
}
