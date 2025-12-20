import java.util.Hashtable;

public class SymbolTable {

	public class Sym {
		public Sym(String id) {
			myName = id;
		}

		public String name() {
			return myName;
		}

		public String toString() {
			return myName;
		}

		// private fields
		private String myName;
	};

	Hashtable<String, String> table;

	SymbolTable() {
		table = new Hashtable<String, String>();
	}

	public void insertFull(String name, String sym) {
		table.put(name, sym);
	}

	// public Sym lookup(String name) {
	// 	return (Sym) table.get(name);
	// }

	// public Sym insert(String name) {
	// 	if (table.containsKey(name))
	// 		return (Sym) table.get(name);
	// 	Sym sym = new Sym(name);
	// 	table.put(name, sym);
	// 	return sym;
	// }

	public String lookup(String name) {
		return table.get(name);
	}

	public boolean hasDuplicate(String name) {
		return table.containsKey(name);
	}
}
