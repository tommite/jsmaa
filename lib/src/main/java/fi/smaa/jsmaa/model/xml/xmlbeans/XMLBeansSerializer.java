package fi.smaa.jsmaa.model.xml.xmlbeans;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

import noNamespace.AffineLinearFunctionType;
import noNamespace.AlternativeType;
import noNamespace.CardinalDomainType;
import noNamespace.CategoryType;
import noNamespace.DomainType;
import noNamespace.OutrankingCriterionType;
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
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class XMLBeansSerializer {

	public XMLBeansSerializer() {
		
	}
	
	public SMAATRIModel deSerialize(String xml) throws XmlException {
		SMAATRIModelDocument doc = SMAATRIModelDocument.Factory.parse(xml);
		SMAATRIModel model = new SMAATRIModel("from xmcda");
		
		Categories cats = doc.getSMAATRIModel().getCategories();
		for (CategoryType c : cats.getCategoryList()) {
			model.addCategory(new Alternative(c.getName()));
		}

		Alternatives alts = doc.getSMAATRIModel().getAlternatives();
		for (AlternativeType a : alts.getAlternativeList()) {
			model.addAlternative(new Alternative(a.getName()));
		}
		
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
		
		return model;
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
		StringWriter writer = new StringWriter();
		doc.save(writer, new XmlOptions().setSavePrettyPrint());
		String res =  writer.toString();
		writer.close();
		return res;
	}
	
	private void addCriteria(SMAATRIModel model, noNamespace.SMAATRIModelDocument.SMAATRIModel dmod) throws NonserializableModelException {
		Criteria crit = dmod.addNewCriteria();
		for (Criterion c : model.getCriteria()) {
			OutrankingCriterion o = (OutrankingCriterion) c;
			OutrankingCriterionType ot = crit.addNewCriterion();
			ot.setName(o.getName());
			// set domain
			DomainType d = ot.addNewDomain();
			CardinalDomainType cd = (CardinalDomainType) d.changeType(CardinalDomainType.type);
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
