/*
    Documentation:
    
        The API object contains following:
        -ids
            Each object must have a unique id, these values define the start of the ID
            An ID consists of TYPE|FUNCTION|PROCEDURE|EXAMPLE_F|EXAMPLE_P + index of the object in the list
        - types
            id.........Is a unique ID in this file! Must consist of 'p' plus index of the entry
            name.......The name of the data type
            descr......A brief description of this data type
        - functions
            id.........Is a unique ID in this file! Must consist of 'p' plus index of the entry
            name.......The name of the function, e.g. coefficientAt
            fullname...The full name of the function with parameter and return types, e.g. function coefficientAt(f: Type1, i: Type2): Boolean
            param......An array with parameter types
            descr......A brief description of this function
        - procedures
            id.........Is a unique ID in this file! Must consist of 'p' plus index of the entry
            name.......The name of the procedure
            fullname...The full name of the procedure, with parameter types.
            param......An array of parameters
            descr......A brief description of the procedure
        -examples
            Contains an array of examples for functions and procedures
        -snippets
            Currently empty
            
        -functions to get functions/procedures
 */
API = {
    ids: {
        TYPE: "t",
        FUNCTION: "f",
        PROCEDURE: "proc",
        PROGRAM: "prog",
        EXAMPLE_F: "ef",
        EXAMPLE_P: "ep",
        SNIPPET: "s"
    },
    types: [
        {
            id: "t1",
            name: "Integer",
            descr: "Repr&auml;sentiert einen Integer-Wert."
        },
        {
            id: "t2",
            name: "Polynomial",
            descr: "Repr&auml;sentiert ein Polynom."
        },
        {
            id: "t3",
            name: "Z(p)",
            descr: "nRepr&auml;sentiert eine Restklasse modulo p."
        },
        {
            id: "t4",
            name: "String",
            descr: "Repr&auml;sentiert eine Zeichenkette."
        },
        {
            id: "t5",
            name: "Z(p)[x]",
            descr: "Repr&auml;sentiert einen Polynomring modulo p."
        },
        {
            id: "t6",
            name: "GF(p, g)",
            descr: "Repr&auml;sentiert ein Galois Feld mit Charakteristik p und irreduziblen Polynom g."
        },
        {
            id: "t7",
            name: "PseudoRandomGenerator(seed, max)",
            descr: "Repr&auml;sentiert einen Pseudozufallszahlen-Generator mit Startwert seed.\nGeneriert Pseudozufallszahlen zwischen 0 und max."
        },
        {
            id: "t8",
            name: "RandomGenerator(max)",
            descr: "Repr&auml;sentiert einen Zufallszahlen-Generator.\nGeneriert Zufallszahlen zwischen 0 und max."
        },
        {
            id: "t9",
            name: "RandomGenerator(min : max)",
            descr: "Repr&auml;sentiert einen Zufallszahlen-Generator.\nGeneriert Zufallszahlen zwischen min und max."
        },
        {
            id: "t10",
            name: "Record",
            descr: "Keine Beschreibung vorhanden."
        }
    ],
    functions: [
        {
            id: "f1",
            name: "coefficientAt",
            fullname: "function coefficientAt(f: Type1, i: Type2): Boolean",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Z()[x] | GF() | Polynomial]\nType2:= [Integer | Prime]\n\nBestimmt den Koeffizienten von x^e in ply."
        },
        {
            id: "f2",
            name: "degree",
            fullname: "function degree(f: Type): Integer",
            param: ["Type"],
            descr: "Type:= [Z()[x] | GF() | Polynomial]\n\nBestimmt den Grad von ply."
        },
        {
            id: "f3",
            name: "eea",
            fullname: "function eea(a: Type1, b: Type2) : Integer[]",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nRetourniert das Array {d, s, t} wobei s*a + t*b = gcd(a, b) = d ist."
        },
        {
            id: "f4",
            name: "eea",
            fullname: "function eea(f: Z()[x], g: Z()[x]) : Z()[x][]",
            param: ["Z()[x]", "Z()[x]"],
            descr: "Retourniert das Array {d, s, t} wobei s*g + t*h = gcd(g, h) = d in Z()[x] ist."
        },
        {
            id: "f5",
            name: "evaluatePolynomial",
            fullname: "function evaluatePolynomial(f: Type1, x: Type2): Integer",
            param: ["Type1", "Type2"],
            descr: "Type1:= [GF() | Polynomial | Z()[x]]\nType2:= [Integer | Prime]\n\nBerechnet den Wert von f, mit x:=val."
        },
        {
            id: "f6",
            name: "factor",
            fullname: "function factor(f: GF()): Polynomial[][]",
            param: ["GF()"],
            descr: "Bestimmt die Faktorisierung von f in GF."
        },
        {
            id: "f7",
            name: "factorInteger",
            fullname: "function factorInteger(n: Integer): Integer[][]",
            param: ["Integer"],
            descr: "Faktorisiert eine Zahl n. Wechselt zwischen Pollard's rho, Pollard's p-1 und simpler Iteration."
        },
        {
            id: "f8",
            name: "factorSquareFree",
            fullname: "function factorSquareFree(f: GF()): Polynomial[][]",
            param: ["GF()"],
            descr: "Bestimmt die quadratfreie Faktorisierung von f in GF."
        },
        {
            id: "f9",
            name: "gcd",
            fullname: "function gcd(f: Z()[x], g: Z()[x]: Z()[x][]",
            param: ["Z()[x]", "Z()[x]"],
            descr: "Bestimmt den gr&ouml;&szlig;ten gemeinsamen Teiler a und b in Z()[x]."
        },
        {
            id: "f10",
            name: "gcd",
            fullname: "function gcd(a: Type1, b: Type2): Integer[]",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nBestimmt den gr&ouml;&szlig;ten gemeinsamen Teiler a und b."
        },
        {
            id: "f11",
            name: "getCharacteristic",
            fullname: "function getCharacteristic(f: Type): Integer",
            param: ["Type"],
            descr: "Type:= [Z()[x] | GF() | Z()]\n\nBestimmt die Charakteristik von [GF() | Z() |Z()[x]] des Wertes val."
        },
        {
            id: "f12",
            name: "getIrreduciblePolynomial",
            fullname: "function getIrreduciblePolynomial(f: GF()): Polynomial",
            param: ["GF()"],
            descr: "Bestimmt das irreduzible Polynom von GF() des Wertes val."
        },
        {
            id: "f13",
            name: "getNextPrime",
            fullname: "function getNextPrime(a: Type): Prime",
            param: ["Type"],
            descr: "Type:= [Integer | Prime]\n\nBestimmt die n&auml;chste Primzahl nach dem Wert val."
        },
        {
            id: "f14",
            name: "int",
            fullname: "function int(a: Z()): Integer",
            param: ["Z()"],
            descr: "Konvertiert Wert val in Z(p) zu Integer. Information zur Restklasse geht verloren."
        },
        {
            id: "f15",
            name: "irreduciblePolynomial",
            fullname: "",
            param: ["Type1", "Type2"],
            descr: "function irreduciblePolynomial(n: Type1, p: Type2): Polynomial\n\nType1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nGeneriert ein irreduzibles Polynom vom Grad n modulo p."
        },
        {
            id: "f16",
            name: "isIrreducible",
            fullname: "function isIrreducible(f: Z()[x]): Boolean",
            param: ["Z()[x]"],
            descr: "Retourniert true falls val irreduzibel ist in Z()[x], false andernfalls."
        },
        {
            id: "f17",
            name: "isIrreducible",
            fullname: "function isIrreducible(f: Polynomial, p: Type): Boolean",
            param: ["Polynomial", "Type"],
            descr: "Type:= [Integer | Prime]\n\nRetourniert true falls ply irreduzibel ist in Z(p)[x], false andernfalls."
        },
        {
            id: "f18",
            name: "isPrime",
            fullname: "function isPrime(a: Integer): Boolean",
            param: ["Integer"],
            descr: "Retourniert true falls val prim ist, false andernfalls."
        },
        {
            id: "f19",
            name: "isPrimitive",
            fullname: "function isPrimitive(ply: Polynomial, p: Type, primeFactors: Prime[]): Boolean",
            param: ["Polynomial", "Type", "Prime[]"],
            descr: "Type:= [Integer | Prime]\n}Retourniert true falls ply primitiv ist in Z(p)[x], false andernfalls.\nprimeFactors:= Primfaktoren von p^(Grad von ply)-1."
        },
        {
            id: "f20",
            name: "isPrimitive",
            fullname: "function isPrimitive(f: Z()[x]): Boolean",
            param: ["Z()"],
            descr: "Retourniert true falls ply primitiv ist in Z(p)[x], false andernfalls.\np^(Grad von ply)-1 wird in Primfaktoren zerlegt (kann unter Umst&auml;nden einige Zeit beanspruchen)."
        },
        {
            id: "f21",
            name: "isPrimitive",
            fullname: "function isPrimitive(f: Z()[x]): Boolean",
            param: ["Z()[x]"],
            descr: "Retourniert true falls ply primitiv ist in Z(p)[x], false andernfalls.\np^(Grad von ply)-1 wird in Primfaktoren zerlegt (kann unter Umst&auml;nden einige Zeit beanspruchen)."
        },
        {
            id: "f22",
            name: "isPrimitive",
            fullname: "function isPrimitive(f: Polynomial, p: Type): Boolean",
            param: ["Polynomial", "Type"],
            descr: "Type:= [Integer | Prime]\n\nRetourniert false falls ply primitive ist in Z(p)[x], false andernfalls.\np^(Grad von ply)-1 wird in Primfaktoren zerlegt (kann unter Umst&auml;nden einige Zeit beanspruchen)."
        },
        {
            id: "f23",
            name: "lcm",
            fullname: "function lcm(a: Type1, b: Type2): Integer",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nBestimmt das kleinste gemeinsame Vielfache von a und b."
        },
        {
            id: "f24",
            name: "leadingCoefficient",
            fullname: "function leadingCoefficient(f: Type): Integer",
            param: ["Type"],
            descr: "Type:= [Z()[x] | GF() | Polynomial]\n\nBestimmt den Leitkoeffizienten von ply."
        },
        {
            id: "f25",
            name: "max",
            fullname: "function max(a: Type1, b: Type2): Integer",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nBestimmt das Maximum von a und b."
        },
        {
            id: "f26",
            name: "min",
            fullname: "function min (a: Type1, b: Type2): Integer",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\nType2:= [Integer | Prime]\n\nBestimmt das Minimum von a und b."
        },
        {
            id: "f27",
            name: "ply",
            fullname: "function ply(f: Type): Polynomial",
            param: ["Type"],
            descr: "Type:= [Z()[x] | GF()]\n\nKonvertiert Wert val zu Polynomial."
        },
        {
            id: "f28",
            name: "randomPolynomial",
            fullname: "function randomPolynomial(n: Type1, p: Type2): Polynomial",
            param: ["Type1", "Type2"],
            descr: "Type1:= [Integer | Prime]\n Type2:= [Integer | Prime]\n\nGeneriert ein zuf&auml;lliges Polynom vom Grad n modulo p."
        }
    ],
    procedures: [
        {
            id: "proc1",
            name: "print",
            fullname: "procedure print(val: Type)",
            param: ["Type"],
            descr: "Type:= [Integer | String| Prime | Z() | Z()[x] | GF() | PseudoRandomGenerator | RandomGenerator]\n\nGibt val auf der Konsole aus."
        },
        {
            id: "proc2",
            name: "println",
            fullname: "procedure println(val: Type)",
            param: ["Type"],
            descr: "Type:= [Integer | String| Prime | Z() | Z()[x] | GF() | PseudoRandomGenerator | RandomGenerator]\n\nGibt val und neue Zeile auf der Konsole aus."
        }
    ],
    programs: [
    	// changed 03.04.2019 - changes single quotes inside print(ln) to double quotes (by escaping the double quotes -> \" )
    	{
            id: "prog1",
            name: "HelloWorld",
            code: "program HelloWorld{\n" +
                "	println(\"Hello World!\");\n" +
                "}"
        },
    	{
            id: "prog2",
            name: "RSAExample",
            code: "program RSAExample{\n" +
                "	const p: Prime := getNextPrime(10^9);	\n" +
                "	const q: Prime := getNextPrime(p);\n" +
                "	const n: Integer := p*q;	\n" +
                "	const phi: Integer := (p-1)*(q-1);	\n" +
                "	m,c: Z(n);\n" +
                "	e,d: Z(phi);	\n" +
                "	X: RandomGenerator(n);\n" +
                "	m := 123456789;	// this is our message\n" +
                "	e := 2*X+1;    // draw an odd random number	\n" +
                "	d := 1/e;  // determine e such that e*d = 1 (mod n)	\n" +
                "	c := m^e;  // encryption of the message m	\n" +
                "	m := c^d;  // decryption of the ciphertext c\n" +
                "	println(\"c = \" + c);\n" +
                "	println(\"m = \" + m);\n" +
                "}"
        },
        {
            id: "prog3",
            name: "MillerRabin",
            code: "program MillerRabin{\n" +
                "	function MillerRabinTest(n: Integer; k: Integer) : Boolean {\n" +
                "		s, r, a, j: Integer;\n" +
                "		y: Z(n);\n" +
                "		result: Boolean;\n" +
                "		result := true;\n" +
                "		r := n - 1;\n" +
                "		s := 0;\n" +
                "		\n" +
                "		while(r MOD 2 == 0) {\n" +
                "			s := s + 1;\n" +
                "			r := r / 2;\n" +
                "		}\n" +
                "		\n" +
                "		for iter = 0 to k {\n" +
                "			a := Random(2:n-2);	// draw new random value\n" +
                "			y := a^r;\n" +
                "			if (y != 1 AND y != n-1) {\n" +
                "				j := 1;\n" +
                "				while(j <= (s-1) AND y != (n-1)) {\n" +
                "					y := y^2;\n" +
                "					if (y == 1) {\n" +
                "						result := false;\n" +
                "						break;\n" +
                "					}\n" +
                "					j := j + 1;\n" +
                "				}\n" +
                "				if (y != (n-1)) {\n" +
                "					result := false;\n" +
                "					break;\n" +
                "				}\n" +
                "			}\n" +
                "		}\n" +
                "		return result;\n" +
                "	}\n" +
                "	\n" +
                "	n, k: Integer;\n" +
                "	n := 1234567897156846189751687946494587238423904657; //2^10*7+1; //9787;\n" +
                "	k := 10;\n" +
                "	println(\"Miller-Rabin primality test for n = \" + n + \" yields \" + MillerRabinTest(n,k));\n" +
                "}"
        },
        {
            id: "prog4",
            name: "IrreduzibelPrimitiv",
            code: "program IrreduciblePrimitive {\n" +
                "	/** \n" +
                "	* Testing polynomials for irreducibility\n" +
                "	* Algorithm 4.69 from the Handbook of Applied Cryptography \n" +
                "	*/\n" +
                "	function isIrreducibleQ(f : Polynomial; p : Prime) : Boolean{\n" +
                "		result : Boolean;\n" +
                "		u, d, ff : Z(p)[x];\n" +
                "    		lc: Z(p);\n" +
                "		m: Integer;\n" +
                "		ff := f;\n" +
                "	\n" +
                "		if(leadingCoefficient(ff) > 1){\n" +
                "			//not normalized\n" +
                "    	    		lc := leadingCoefficient(ff);\n" +
                "    	    		ff := ff * lc^-1;\n" +
                "		}\n" +
                "		\n" +
                "		result := true;\n" +
                "    		m := degree(ff);\n" +
                "		u := [x]; // u(x) := x\n" +
                "	   	if(m != 1){\n" +
                "			for i = 1 to m/2 { // for i to m/2\n" +
                "				u := u^p MOD ff;  // u(x) := u(x)^p mod f(x)\n" +
                "				d := gcd(ff, u - [x]); //d(x) := gcd(f(x), u(x) - x)\n" +
                "           			if(degree(d) > 0){ // if d(x) != 1, then reducible\n" +
                "					result := false; \n" +
                "					i := m;\n" +
                "				}\n" +
                "			}\n" +
                "		}\n" +
                "		return result;\n" +
                "	}\n" +
                "	\n" +
                "	/** \n" +
                "	* Tests if a polynomial is primitive \n" +
                "	* Algorithm 4.77 from the Handbook of Applied Cryptography \n" +
                "	*/\n" +
                "	function isPrimitiveQ(f : Polynomial; p : Prime) : Boolean{\n" +
                "		result : Boolean;\n" +
                "		u, d, ff, lx : Z(p)[x];\n" +
                "    		lc: Z(p);\n" +
                "		m, pm, val: Integer;\n" +
                "    		primeFactors : Integer[][];\n" +
                "		ff := f;\n" +
                "	\n" +
                "		if(isIrreducibleQ(f, p)){\n" +
                "			m:= degree(f);\n" +
                "			pm := p^m - 1;\n" +
                "			\n" +
                "			if(leadingCoefficient(ff) > 1){\n" +
                "				//not normalized\n" +
                "        			lc := leadingCoefficient(ff);\n" +
                "        			ff := ff * lc^-1;\n" +
                "			}\n" +
                "			\n" +
                "			primeFactors := factorInteger(pm); //get the factorization of p^m - 1\n" +
                "            		result := true;\n" +
                "			for i = 0 to #primeFactors - 1{\n" +
                "				val := primeFactors[i][0]; \n" +
                "				lx := [x]^(pm/val) MOD ff; //l(x) := x^((p^m - 1) / r_i) mod f(x)\n" +
                "				if(lx == 1){ // not primitive\n" +
                "					result := false;\n" +
                "					i := #primeFactors;\n" +
                "				}\n" +
                "			}\n" +
                "		}else{ // reducible, hence not primitive\n" +
                "			result := false;\n" +
                "		}\n" +
                "  		return result;\n" +
                "	}\n" +
                "	\n" +
                "	p : Prime;\n" +
                "	ply : Polynomial;\n" +
                "	pr : Z(3)[x];\n" +
                "	ply := [1 + x + 2x^2];\n" +
                "	p := 3;\n" +
                "	\n" +
                "	println(\"irreducible? -> \" + isIrreducibleQ(ply, p));\n" +
                "	println(\"primitive? -> \" + isPrimitiveQ(ply, p));\n" +
                "	\n" +
                "	println(\"\nx^i for i = 1 ... \" + (p^degree(ply) - 1) + \":\");\n" +
                "	pr := [x];\n" +
                "	for i = 1 to p^degree(ply) - 1 {\n" +
                "		println(pr^i MOD ply);\n" +
                "	}\n" +
                "}"
        },
        {
            id: "prog5",
            name: "Polynomials",
            code: "program calculate{\n" +
                "	x,a: Integer;\n" +
                "	x:= 3;\n" +
                "	a:= 4;\n" +
                "	println([1 +  x + x^2]);\n" +
                "	println([1 +  (x)]);\n" +
                "	println([1 + (a + 1) x + x^2]);\n" +
                "	println([1 + (x) x + x^2]);\n" +
                "	println([x^x]);\n" +
                "	println([(x)^x]);\n" +
                "	\n" +
                "}"
        },
        {
            id: "prog6",
            name: "Arrays",
            code: "program calculate{\n" +
                "	a: Prime[][];\n" +
                "	b: Z(3)[];\n" +
                "	\n" +
                "	a:= {{2,5,7},{3,11,13}};\n" +
                "	b:= {1,2,3,4,5,6,7,8};\n" +
                "	\n" +
                "	println(a);\n" +
                "	println(b);\n" +
                "	\n" +
                "}"
        },
        {
            id: "prog7",
            name: "StandardElGamal",
            code: "program StandardElGamal{\n" +
                "	const p : Prime := 2063;\n" +
                "	const g : Integer := 607; //Generator \n" +
                "	function encrypt(m: Integer; pk: Integer) : Z()[] {\n" +
                "		c: Z(p)[];\n" +
                "		X: RandomGenerator(1:p-1);\n" +
                "		r: Integer;\n" +
                "		c := new Z()[2];\n" +
                "		r := X;\n" +
                "		c[0] := g^r;\n" +
                "		c[1] := m * pk^r;\n" +
                "		return c;\n" +
                "	}\n" +
                "	function decrypt(c: Z()[]; sk: Integer) : Z() {\n" +
                "		return c[1] * c[0]^(-sk);\n" +
                "	}\n" +
                "	\n" +
                "	X: RandomGenerator(1:(p-1));\n" +
                "	z: Integer;\n" +
                "	h: Integer;\n" +
                "	message: Integer;\n" +
                "	ciphertext: Z(p)[];\n" +
                "	message := 123;\n" +
                "	z := X;\n" +
                "	h := g^z;\n" +
                "	ciphertext := encrypt(message,h);\n" +
                "	println(\"message: \" + message);\n" +
                "	println(\"ciphertext: \" + ciphertext);\n" +
                "	println(\"decrypted into: \" + str(decrypt(ciphertext,z)));\n" +
                "}"
        },
        {
            id: "prog8",
            name: "ECCEIGamal",
            code: "program ECCElGamal{\n" +
                "	dr, kr: RandomGenerator(10^3:8831);	// random generators\n" +
                "	s, k: Integer;\n" +
                "	G, P, M, D, C1, C2: EC(Z(8831), a4 := 3, a6 := 45);\n" +
                "	G := << 4 , 11 >>;\n" +
                "	//Alice generates Keys\n" +
                "	s := dr;	// random secret key\n" +
                "	P := s * G;	// compute the public key\n" +
                "		\n" +
                "	//Bob encrypts a message\n" +
                "	M := << 7168 , 3452 >>;\n" +
                "	k := kr;\n" +
                "	C1 := k * G;\n" +
                "	C2 := M + k * P;\n" +
                "		\n" +
                "	println(\"Original Message: \" + M);\n" +
                "	println(\"Encrypted Message: (\" + str(C1) + \",\" + str(C2) + \")\");\n" +
                "	//Alice decrypts\n" +
                "	D := C2 - s * C1;\n" +
                "	println(\"Decrypted Message: \" + D);	\n" +
                "}"
        },
        {
            id: "prog9",
            name: "DiffieHellman",
            code: "program DiffieHellman{\n" +
                "		\n" +
                "	const p : Prime :=  12232282722332322333323442234344333353322222222671; //Modul 50. stellig\n" +
                "	const g : Integer := 3; //Generator \n" +
                "		\n" +
                "	/** Alice */\n" +
                "	alice : Record \n" +
                "			a : Integer; //Geheimzahl\n" +
                "			g : Integer; //Generator\n" +
                "			A : Z(p); //Kommunikationsparameter\n" +
                "			K : Z(p); //gemeinsamer Schlüssel\n" +
                "		EndRecord;\n" +
                "	\n" +
                "	/** Bob */	\n" +
                "	bob : 	Record \n" +
                "			b : Integer; //Geheimzahl\n" +
                "			B : Z(p); //Kommunikationsparameter\n" +
                "			K : Z(p); //gemeinsamer Schlüssel \n" +
                "		EndRecord;\n" +
                "		\n" +
                "	/*\n" +
                "	 * Alice\n" +
                "	*/\n" +
                "	println(\"Alice: \");	\n" +
                "	alice.a := Random(2 : p-2);\n" +
                "	println(\"  Wählt zufällige Geheimzahl a = \" + alice.a);\n" +
                "	alice.g := g;\n" +
                "	println(\"  Generator g = \" + alice.g);\n" +
                "	alice.A := alice.g^alice.a;\n" +
                "	println(\"  Berechnet A = g^a = \" + alice.A);\n" +
                "	println(\"  sendet (g, p, A) = (\" + alice.g + \", \" + p + \", \" \n" +
                "		+ alice.A + \") an Bob.\");\n" +
                "	\n" +
                "	/*\n" +
                "	 * Bob\n" +
                "	*/\n" +
                "	println(\"\nBob: \");	\n" +
                "	bob.b := Random(2 : p-2);\n" +
                "	println(\"  Wählt zufällige Geheimzahl b = \" + bob.b);\n" +
                "	bob.B := alice.g^bob.b;\n" +
                "	println(\"  Berechnet B = g^b = \" + bob.B);\n" +
                "	println(\"  sendet (B) = (\" + bob.B + \") an Alice.\");\n" +
                "	bob.K := alice.A^bob.b;\n" +
                "	println(\"  Berechnet K = A^b = \" + bob.K);\n" +
                "	\n" +
                "	/*\n" +
                "	 * Alice\n" +
                "	*/\n" +
                "	println(\"\nBob: \");\n" +
                "	alice.K := bob.B^alice.a;	\n" +
                "	println(\"  Berechnet K = B^a = \" + alice.K);\n" +
                "	\n" +
                "	\n" +
                "}"
        }
        ],
    examples: {
        functions: [
            {
                id: "ef1",
                name: "faculty(Integer)",
                fullname: "function faculty(val: Integer): Integer",
                descr: "Berechne die Fakult&auml;t von val."
            },
            {
                id: "ef2",
                name: "facultyRecursive(Integer)",
                fullname: "function facultyRecursive(val: Integer): Integer",
                descr: "Berechne die Fakult&auml;t von val rekursiv."
            },
            {
                id: "ef3",
                name: "funcTemplate()",
                fullname: "function funcTemplate(): Integer",
                descr: "Template Funktion."
            },
            {
                id: "ef4",
                name: "isIrreducibleQ(Polynomial, Prime)",
                fullname: "function isIrreducibleQ(f: Polynomial, p: Prime): Boolean",
                descr: "Testet Polynom f auf Irreduzibilität in Z(p)[x]"
            },
            {
                id: "ef5",
                name: "isPrimitiveQ(Polynomial, Prime)",
                fullname: "function isPrimitiveQ(f: Polynomial, p: Prime): Boolean",
                descr: "Testet ob Polynom f primitv in Z(p)[x] ist."
            }
        ],
        procedures: [
            {
                id: "ep6",
                name: "procTemplate()",
                fullname: "procedure procTemplate()",
                descr: "Template Prozedur."
            }
        ]

    },
    snippets: [
            // testing 03.04.2019
            {
                id:         "s1",
                name:       "snippet1",
                fullname:   "somesnippet1",      
                descr:      "Description for snippet 1"
            }
],

    getFunctionById: function (id) {
        for (var i = 0, f; f = this.functions[i]; i++) {
            if (id === f.id) {
                return f;
            }
        }
        return 0;
    },
    // @Param obj   An object of type function, can be a member of the functions array
    // @return  Returns a pretty string of the function.
    getFunctionNameByObject: function (obj) {
        return obj.name + "(" + obj.param + ")";
    },
    // @Param obj   An object of type prozedure, can be a member of the prozedure array
    // @return  returns a pretty string of the prozedure.
    getProcedureNameByObject: function (obj) {
        return obj.name + "(" + obj.param + ")";
    },
    // @Param id    The unique id of an API object (of types, functions, procedures, ...)
    // @return  Returns the object with the specified id or 0
    getObjectById: function (id) {
        var objectsToSearch;
        if (id.substring(0, this.ids.TYPE.length) === this.ids.TYPE) {
            objectsToSearch = this.types;
        } else if (id.substring(0, this.ids.FUNCTION.length) === this.ids.FUNCTION) {
            objectsToSearch = this.functions;
        } else if (id.substring(0, this.ids.PROCEDURE.length) === this.ids.PROCEDURE) {
            objectsToSearch = this.procedures;
        } else if (id.substring(0, this.ids.PROGRAM.length) === this.ids.PROGRAM) {
            objectsToSearch = this.programs;
        } else if (id.substring(0, this.ids.EXAMPLE_F.length) === this.ids.EXAMPLE_F) {
            objectsToSearch = this.examples.functions;
        } else if (id.substring(0, this.ids.EXAMPLE_P.length) === this.ids.EXAMPLE_P) {
            objectsToSearch = this.examples.procedures;
        } else if (id.substring(0, this.ids.SNIPPET.length) === this.ids.SNIPPET) {
            objectsToSearch = this.snippets;
        }
        if (objectsToSearch) {
            for (var i = 0, o; o = objectsToSearch[i]; i++) {
                if (id == o.id) {
                    return o;
                }
            }
        }
        return 0;
    },
    // @return  Returns all objects of types, functions, procedures, examples->functions, examples->procedures, snippets
    getAllObjects: function () {
        var objects = [];
        objects = objects.concat(this.types);
        objects = objects.concat(this.functions);
        objects = objects.concat(this.procedures);
        objects = objects.concat(this.programs);
        objects = objects.concat(this.examples.functions);
        objects = objects.concat(this.examples.procedures);
        objects = objects.concat(this.snippets);
        return objects;
    },

    // @return  Returns the pretty-name of an object
    getName: function (obj) {
        var id = obj.id;
        var name = obj.name;
        if (id.substring(0, this.ids.FUNCTION.length) === this.ids.FUNCTION) {
            name = API.getFunctionNameByObject(obj);
        } else if (id.substring(0, this.ids.PROCEDURE.length) === this.ids.PROCEDURE) {
            name = API.getProcedureNameByObject(obj);
        }
        return name;
    }
}
