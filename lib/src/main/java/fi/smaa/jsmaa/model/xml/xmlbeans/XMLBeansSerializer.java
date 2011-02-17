package fi.smaa.jsmaa.model.xml.xmlbeans;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import noNamespace.AffineLinearFunctionType;
import noNamespace.AlternativeType;
import noNamespace.CardinalDomainType;
import noNamespace.CategoryType;
import noNamespace.DeterministicValueType;
import noNamespace.DomainType;
import noNamespace.GaussianType;
import noNamespace.IntervalType;
import noNamespace.MeasurementType;
import noNamespace.OutrankingCriterionType;
import noNamespace.PerformancesType;
import noNamespace.PerformancesType.Performance;
import noNamespace.Point2DType;
import noNamespace.SMAATRIModelDocument;
import noNamespace.SMAATRIModelDocument.SMAATRIModel.Alternatives;
import noNamespace.SMAATRIModelDocument.SMAATRIModel.Categories;
import noNamespace.SMAATRIModelDocument.SMAATRIModel.Criteria;
import noNamespace.SMAATRIModelDocument.SMAATRIModel.Profiles;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.Measurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class XMLBeansSerializer {

	public XMLBeansSerializer() {
	}
	
	public SMAATRIModel deSerialize(String xml) throws XmlException {
		SMAATRIModelDocument doc = SMAATRIModelDocument.Factory.parse(xml);
		SMAATRIModel model = new SMAATRIModel("from xmcda");
		
		addCategories(doc, model);
		addAlternatives(doc, model);		
		addCriteria(doc, model);
		addAlternativePerformances(doc, model);
		addProfilePerformances(doc, model);
		
		return model;
	}

	private void addProfilePerformances(SMAATRIModelDocument doc, SMAATRIModel model) {
		PerformancesType perf = doc.getSMAATRIModel().getProfilePerformances();
		List<Alternative> alts = model.getCategories();
		List<Criterion> crit = model.getCriteria();
		
		for (Performance p : perf.getPerformanceList()) {
			Alternative a = getCategoryFromProfileName(p.getAlternative().getRef());
			Criterion c = findCriterionWithName(crit, p.getCriterion().getRef());
			CardinalMeasurement meas = makeMeasurement(p.getMeasurement());
			model.setCategoryUpperBound((OutrankingCriterion) c, a, meas);
		}
		
	}

	private void addAlternativePerformances(SMAATRIModelDocument doc, SMAATRIModel model) {
		PerformancesType perf = doc.getSMAATRIModel().getAlternativePerformances();
		List<Alternative> alts = model.getAlternatives();
		List<Criterion> crit = model.getCriteria();
		
		for (Performance p : perf.getPerformanceList()) {
			Alternative a = findAlternativeWithName(alts, p.getAlternative().getRef());
			Criterion c = findCriterionWithName(crit, p.getCriterion().getRef());
			Measurement meas = makeMeasurement(p.getMeasurement());
			model.setMeasurement(c, a, meas);
		}
		
	}

	private Measurement makeMeasurement(MeasurementType measurement) {
		if (measurement instanceof DeterministicValueType) {
			DeterministicValueType dv = (DeterministicValueType) measurement;
			return new ExactMeasurement(dv.getValue().doubleValue());
		} else if (measurement instanceof IntervalType) {
			IntervalType ival = (IntervalType) measurement;
			return new Interval(ival.getBegin().doubleValue(), ival.getEnd().doubleValue());
		} else if (measurement instanceof GaussianType) {
			GaussianType g = (GaussianType) measurement;
			return new GaussianMeasurement(g.getMu(), g.getSigma());
		}
		throw new IllegalStateException("Unknown measurement type " + measurement.getClass().getCanonicalName());
	}

	private Alternative findAlternativeWithName(List<Alternative> alts, String ref) {
		for (Alternative a : alts) {
			if (a.getName().equals(ref)) {
				return a;
			}
		}
		throw new IllegalStateException("No alternative with name " + ref);
	}
	
	private Criterion findCriterionWithName(List<Criterion> crit, String ref) {
		for (Criterion a : crit) {
			if (a.getName().equals(ref)) {
				return a;
			}
		}
		throw new IllegalStateException("No criterion with name " + ref);
	}	

	private void addCriteria(SMAATRIModelDocument doc, SMAATRIModel model) {
		Criteria crit = doc.getSMAATRIModel().getCriteria();
		for (OutrankingCriterionType o : crit.getCriterionList()) {
			DomainType d = o.getDomain();
			boolean asc = true;
			if (d instanceof CardinalDomainType) {
				asc = ((CardinalDomainType) d).getAscendingPreference();
			}
			OutrankingCriterion oc = new OutrankingCriterion(o.getName(),
					asc, getThreshold(o.getIndifferenceThreshold()),
					getThreshold(o.getPreferenceThreshold()));
			model.addCriterion(oc);
		}
	}

	private void addAlternatives(SMAATRIModelDocument doc, SMAATRIModel model) {
		Alternatives alts = doc.getSMAATRIModel().getAlternatives();
		for (AlternativeType a : alts.getAlternativeList()) {
			model.addAlternative(new Alternative(a.getName()));
		}
	}

	private void addCategories(SMAATRIModelDocument doc, SMAATRIModel model) {
		Categories cats = doc.getSMAATRIModel().getCategories();
		for (CategoryType c : cats.getCategoryList()) {
			model.addCategory(new Alternative(c.getName()));
		}
	}
	
	private ExactMeasurement getThreshold(AffineLinearFunctionType th) {
		return new ExactMeasurement(th.getPointList().get(0).getY().doubleValue());
	}

	public String serialize(SMAATRIModel model) throws IOException, NonserializableModelException {
		SMAATRIModelDocument doc = SMAATRIModelDocument.Factory.newInstance();
		doc.addNewSMAATRIModel();
		noNamespace.SMAATRIModelDocument.SMAATRIModel dmod = doc.getSMAATRIModel();
		addCategories(model, dmod);
		addAlternatives(model, dmod);
		addProfiles(model, dmod);
		addCriteria(model, dmod);
		addAlternativePerformances(model, dmod);
		addProfilePerformances(model, dmod);
		StringWriter writer = new StringWriter();
		doc.save(writer, new XmlOptions().setSavePrettyPrint());
		String res =  writer.toString();
		writer.close();
		return res;
	}
	
	private void addProfilePerformances(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) throws NonserializableModelException {
		PerformancesType perf = dmod.addNewProfilePerformances();
		
		for (int catIndex=0;catIndex<model.getCategories().size()-1;catIndex++) {
			String catName = "p" + (catIndex + 1);
			for (int critIndex=0;critIndex<model.getCriteria().size();critIndex++) {
				String critName = dmod.getCriteria().getCriterionList().get(critIndex).getName();
				
				Measurement m = model.getCategoryUpperBound((OutrankingCriterion) model.getCriteria().get(critIndex), 
						model.getCategories().get(catIndex));
				MeasurementType meas = makeMeasurement(m);
				Performance p = perf.addNewPerformance();
				p.addNewAlternative().setRef(catName);
				p.addNewCriterion().setRef(critName);
				p.setMeasurement(meas);
			}
		}
	}	
	
	private void addAlternativePerformances(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) throws NonserializableModelException {
		PerformancesType perf = dmod.addNewAlternativePerformances();
		
		for (int altIndex=0;altIndex<model.getAlternatives().size();altIndex++) {
			String altName = dmod.getAlternatives().getAlternativeList().get(altIndex).getName();
			for (int critIndex=0;critIndex<model.getCriteria().size();critIndex++) {
				String critName = dmod.getCriteria().getCriterionList().get(critIndex).getName();
				
				Measurement m = model.getMeasurement(model.getCriteria().get(critIndex), 
						model.getAlternatives().get(altIndex));
				MeasurementType meas = makeMeasurement(m);
				Performance p = perf.addNewPerformance();
				p.addNewAlternative().setRef(altName);
				p.addNewCriterion().setRef(critName);
				p.setMeasurement(meas);
			}
		}
	}

	private MeasurementType makeMeasurement(Measurement m) throws NonserializableModelException {
		if (m instanceof ExactMeasurement) {
			ExactMeasurement em = (ExactMeasurement) m;
			DeterministicValueType dvt = DeterministicValueType.Factory.newInstance();
			dvt.setValue(new BigDecimal(em.getValue()));
			return dvt;
		} else if (m instanceof Interval) {
			Interval i = (Interval) m;
			IntervalType ival = IntervalType.Factory.newInstance();
			ival.setBegin(new BigDecimal(i.getStart()));
			ival.setEnd(new BigDecimal(i.getEnd()));
			return ival;
		} else if (m instanceof GaussianMeasurement) {
			GaussianMeasurement g = (GaussianMeasurement) m;
			GaussianType gtyp = GaussianType.Factory.newInstance();
			gtyp.setMu(g.getMean());
			gtyp.setSigma(g.getStDev());
			return gtyp;
		}
		throw new NonserializableModelException("Unknown measurement type " + m.getClass().getCanonicalName());
	}

	private void addCriteria(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) throws NonserializableModelException {
		Criteria crit = dmod.addNewCriteria();
		for (Criterion c : model.getCriteria()) {
			OutrankingCriterion o = (OutrankingCriterion) c;
			OutrankingCriterionType ot = crit.addNewCriterion();
			ot.setName(o.getName());
			// set domain
			CardinalDomainType cd = CardinalDomainType.Factory.newInstance();
			ot.setDomain(cd);
			cd.setAscendingPreference(o.getAscending());
			// set thresholds
			AffineLinearFunctionType indifTh = ot.addNewIndifferenceThreshold();
			setThreshold(indifTh, o.getIndifMeasurement());
			AffineLinearFunctionType prefTh = ot.addNewPreferenceThreshold();
			setThreshold(prefTh, o.getPrefMeasurement());
		}
	}

	private void setThreshold(AffineLinearFunctionType indifTh, CardinalMeasurement th) throws NonserializableModelException {
		if (!(th instanceof ExactMeasurement)) {
			throw new NonserializableModelException("Only exact values allowed for thresholds");
		}
		ExactMeasurement ev = (ExactMeasurement) th;
		Point2DType p1 = indifTh.addNewPoint();
		p1.setX(new BigDecimal(0));
		p1.setY(new BigDecimal(ev.getValue()));
	}

	private void addProfiles(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) {
		Profiles p = dmod.addNewProfiles();
		for (int i=1;i<model.getCategories().size();i++) {
			AlternativeType prof = p.addNewProfile();
			prof.setName("p" + i);
		}
	}

	private void addAlternatives(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) {
		Alternatives a = dmod.addNewAlternatives();
		for (Alternative alt : model.getAlternatives()) {
			AlternativeType atyp = a.addNewAlternative();
			atyp.setName(alt.getName());
		}
	}

	private void addCategories(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) {
		Categories c = dmod.addNewCategories();
		for (Alternative a : model.getCategories()) {
			CategoryType cat = c.addNewCategory();
			cat.setName(a.getName());
		}
	}

}
