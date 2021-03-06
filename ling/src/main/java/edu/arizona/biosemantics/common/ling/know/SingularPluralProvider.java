package edu.arizona.biosemantics.common.ling.know;

import java.util.HashMap;

public class SingularPluralProvider {
	HashMap<String, String> singulars = new HashMap<String, String>();
	HashMap<String, String> plurals = new HashMap<String, String>();
	
	public SingularPluralProvider(){
		//add special cases in BOTH singulars and plurals hashmaps
		//keep the alphabetical order

		singulars.put("anthocyathia", "anthocyathus");
		singulars.put("axis", "axis");
		singulars.put("axes", "axis");
		singulars.put("bases", "base");
		singulars.put("brit", "brit");
		singulars.put("boss", "boss");
		singulars.put("buttress", "buttress");
		singulars.put("callus", "callus");
		singulars.put("catenabe", "catena");
		singulars.put("coremata", "corematis");
		singulars.put("corpora", "corpus");
		singulars.put("crepides", "crepis");
		singulars.put("ephyre", "ephyra");
		singulars.put("ephyrae", "ephyra");
		singulars.put("ephyrula", "ephyra");
		singulars.put("falces", "falx");
		singulars.put("forceps", "forceps");
		singulars.put("fusules", "fusula");
		singulars.put("frons", "frons");
		singulars.put("fry", "fry");
		singulars.put("genera", "genus");
		singulars.put("glochines", "glochis");
		singulars.put("grooves", "groove");
		singulars.put("incudes", "incus");
		singulars.put("interstices", "interstice");
		singulars.put("irises", "iris");
		singulars.put("irides", "iris");
		singulars.put("latera", "latus");
		singulars.put("lens", "len");
		singulars.put("malli", "malleus");
		singulars.put("media", "media");
		singulars.put("metatarsus", "metatarsus");
		singulars.put("metatarsi", "metatarsus");
		singulars.put("midnerves", "midnerve");
		singulars.put("mollusks", "mollusca");
		singulars.put("molluscs", "mollusca");
		singulars.put("parasides", "parapsis");
		singulars.put("perradia", "perradius");
		singulars.put("pharynges", "pharynx");
		singulars.put("pharynxes", "pharynx");
		singulars.put("pileipellis", "pileipellis");
		singulars.put("proboscises", "proboscis");
		singulars.put("process", "process");
		singulars.put("prosoma", "prosoma");
		singulars.put("prosomas", "prosoma");
		singulars.put("ptyxis", "ptyxis");
		singulars.put("proglottides", "proglottis");
		singulars.put("pseudocoelomata", "pseudocoelomates");
		singulars.put("rachis", "rachis");
		singulars.put("series", "series");
		singulars.put("setules", "setula");
		singulars.put("species", "species");
		singulars.put("sperm", "sperm");
		singulars.put("teeth", "tooth");
		singulars.put("tibia", "tibia");
		singulars.put("themselves", "themselves");
		singulars.put("valves", "valve");
	
		
		plurals.put("anthocyathus","anthocyathia");
		plurals.put("axis", "axes");
		plurals.put("base", "bases");
		plurals.put("brit", "brit");
		plurals.put("boss", "bosses");
		plurals.put("buttress", "buttresses");
		plurals.put("callus", "calluses");
		plurals.put("catena","catenabe");
		plurals.put("corematis","coremata");
		plurals.put("corpus","corpora");
		plurals.put("crepis","crepides");
		plurals.put("ephyra","ephyre");
		plurals.put("ephyra","ephyrae");
		plurals.put("ephyra","ephyrula");
		plurals.put("falx","falces");
		plurals.put("forceps", "forceps");
		plurals.put("frons", "fronses");
		plurals.put("fry", "fry");
		plurals.put("fusula","fusules");
		plurals.put("genus","genera");
		plurals.put("glochis","glochines");
		plurals.put("groove", "grooves");
		plurals.put("incus","incudes");
		plurals.put("interstice", "interstices");
		plurals.put("iris","irises");
		plurals.put("iris","irides");
		plurals.put("latus","latera");
		plurals.put("len", "lens");
		plurals.put("malleus","malli");
		plurals.put("media", "media");
		plurals.put("metatarsus", "metatarsi");
		plurals.put("midnerve", "midnerves");
		plurals.put("mollusca","mollusks");
		plurals.put("mollusca","molluscs");
		plurals.put("parapsis","parasides");
		plurals.put("perradius","perradia");
		plurals.put("pharynx","pharynges");
		plurals.put("pharynx","pharynxes");
		plurals.put("pileipellis","pileipellis");		
		plurals.put("proboscis","proboscises");
		plurals.put("proglottis","proglottides");
		plurals.put("process", "processes");
		plurals.put("prosoma", "prosomas");
		plurals.put("pseudocoelomates","pseudocoelomata");
		plurals.put("ptyxis", "ptyxis");
		plurals.put("series", "series");
		plurals.put("setula","setules");
		plurals.put("species", "species");
		plurals.put("sperm", "sperm");
		plurals.put("tibia", "tibiae");
		plurals.put("tooth", "teeth");
		plurals.put("valve", "valves");

	}
	
	public void addPlural(String singular, String plural){
		if(plurals.get(singular)==null)
			plurals.put(singular, plural);
	}
	public void addSingular(String plural, String singular){
		if(singulars.get(plural)==null)
			singulars.put(plural, singular);
	}
	public HashMap<String, String> getSingulars() {
		return singulars;
	}
	public HashMap<String, String> getPlurals() {
		return plurals;
	}
	
}
