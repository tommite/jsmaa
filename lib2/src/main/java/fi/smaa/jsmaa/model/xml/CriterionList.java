package fi.smaa.jsmaa.model.xml;

import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Criterion;

public class CriterionList {

	private List<Criterion> crit;

	public CriterionList(List<Criterion> crit) {
		this.crit = crit;
	}
	
	public CriterionList() {
		crit = new ArrayList<Criterion>();
	}
	
	public List<Criterion> getList() {
		return crit;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<CriterionList> XML = new XMLFormat<CriterionList>(CriterionList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public CriterionList newInstance(Class<CriterionList> cls, InputElement ie) throws XMLStreamException {
			return new CriterionList();
		}
		@Override
		public void read(InputElement ie, CriterionList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Criterion c = ie.get("criterion");
				list.crit.add(c);
			}
		}
		@Override
		public void write(CriterionList list, OutputElement oe) throws XMLStreamException {
			for (Criterion c : list.crit) {
				oe.add(c, "criterion");
			}
		}		
	};			
}
