package fi.smaa.jsmaa.model.xml;

import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Alternative;

public class AlternativeList {

	private List<Alternative> alts;

	public AlternativeList(List<Alternative> alts) {
		this.alts = alts;
	}
	
	public AlternativeList() {
		alts = new ArrayList<Alternative>();
	}
	
	public List<Alternative> getList() {
		return alts;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<AlternativeList> XML = new XMLFormat<AlternativeList>(AlternativeList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public AlternativeList newInstance(Class<AlternativeList> cls, InputElement ie) throws XMLStreamException {
			return new AlternativeList();
		}
		@Override
		public void read(InputElement ie, AlternativeList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Alternative a = ie.get("alternative", Alternative.class);
				list.alts.add(a);
			}
		}
		@Override
		public void write(AlternativeList list, OutputElement oe) throws XMLStreamException {
			for (Alternative a : list.alts) {
				oe.add(a, "alternative", Alternative.class);
			}
		}		
	};			
}
