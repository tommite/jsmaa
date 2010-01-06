package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.MissingPreferenceInformation;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class XmlConfigurator {

	public static XStream getXstream() {
		XStream x = new XStream(new DomDriver());
		x.setMode(XStream.ID_REFERENCES);
		
		configClassNames(x);
		configConverters(x);
		return x;
	}

	private static void configConverters(XStream x) {
		x.registerConverter(new OutrankingCriterionConverter());
		x.registerConverter(new CardinalCriterionConverter());
		x.registerConverter(new CriterionConverter());		
		x.registerConverter(new AlternativeConverter());
		x.registerConverter(new IntervalConverter());
		x.registerConverter(new ExactMeasurementConverter());
		x.registerConverter(new ImpactMatrixConverter());
		x.registerConverter(new RankConverter());
		x.registerConverter(new CardinalPreferenceInformationConverter());
		x.registerConverter(new OrdinalPreferenceInformationConverter());
		x.registerConverter(new MissingPreferenceInformationConverter());
		x.registerConverter(new SMAAModelConverter());
	}

	private static void configClassNames(XStream x) {
		setClassAlias(x, Alternative.class);
		setClassAlias(x, OutrankingCriterion.class);
		setClassAlias(x, OrdinalCriterion.class);
		setClassAlias(x, ScaleCriterion.class);
		setClassAlias(x, Interval.class);
		setClassAlias(x, ExactMeasurement.class);
		setClassAlias(x, ImpactMatrix.class);
		setClassAlias(x, OrdinalPreferenceInformation.class);
		setClassAlias(x, CardinalPreferenceInformation.class);
		setClassAlias(x, MissingPreferenceInformation.class);
		setClassAlias(x, Rank.class);
		setClassAlias(x, SMAAModel.class);
	}

	@SuppressWarnings("unchecked")
	private static void setClassAlias(XStream x, Class c) {
		x.alias(getLabel(c), c);
	}

	@SuppressWarnings("unchecked")
	public static String getLabel(Class c) {
		if (c.equals(OutrankingCriterion.class)) {
			return "outrankingCriterion";
		}
		if (c.equals(OrdinalCriterion.class)) {
			return "ordinalCriterion";
		} 
		if (c.equals(ScaleCriterion.class)) {
			return "cardinalCriterion";
		} 
		if (c.equals(Alternative.class)) {
			return "alternative";
		}
		if (c.equals(Interval.class)) {
			return "interval";
		}
		if (c.equals(ExactMeasurement.class)) {
			return "exact";
		}
		if (c.equals(ImpactMatrix.class)) {
			return "impactMatrix";
		}
		if (c.equals(Rank.class)) {
			return "rank";
		}
		if (c.equals(OrdinalPreferenceInformation.class)) {
			return "ordinalPreferences";
		}
		if (c.equals(CardinalPreferenceInformation.class)) {
			return "cardinalPreferences";		
		}
		if (c.equals(MissingPreferenceInformation.class)) {
			return "missingPreferences";		
		}
		if (c.equals(SMAAModel.class)){
			return "model";
		}
		
		throw new IllegalArgumentException("unknown class " + c);
	}
	
	public static void writeNode(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		writer.startNode(XmlConfigurator.getLabel(obj.getClass()));
		context.convertAnother(obj);
		writer.endNode();
	}
}
