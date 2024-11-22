package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex3Test extends SPLTestBase {
    @Test
    void bankAccountOOP() {
        runSpli("""
		new BankAccount := class {
		new balance := 0;

		new deposit := lambda amt
		    { balance := balance + amt; };

		new withdraw := lambda amt
		    { balance := balance - amt; };
		};

		new rich := BankAccount ! ;
		rich ! deposit @ 1000000 ;

		new poor := BankAccount ! ;
		poor ! deposit @ 10 ;

		rich ! withdraw @ 5;
		poor ! withdraw @ 5;
		write rich ! balance;
		write poor ! balance;
		""",
                List.of("999995", "5"));
    }

    @Test
    void twoClasses() {
        runSpli("""
                new Counter := class {
                    new count := 0;
                    new go := lambda _ {
                        count := count + 1;
                        ret := count;
                    };
                };
                new FastCount := class {
                    new count := 0;
                    new go := lambda _ {
                        count := count + 5;
                        ret := count;
                    };
                };
                new x := Counter!;
                new y := Counter!;
                new z := FastCount!;
                write x!go@0;
                write x!go@0;
                write x!go@0;
                write y!go@0;
                write y!go@0;
                write z!go@0;
                write z!go@0;
                write z!go@0;
                write x!go@0;
                """,
                List.of("1", "2", "3", "1", "2", "5", "10", "15", "4"));
    }

    @Test
    void classInsideFunction() {
        runSpli("""
                new A := class {
                    new x := 10;
                    new y := 20;
                };
                new makeB := lambda bx {
                    ret := class {
                        new x := bx;
                        new y := 100;
                    } !;
                };
                new a1 := A!;
                new b1 := makeB@3;
                new b2 := makeB@7;
                write a1!x;
                write b1!x;
                write b2!x;
                write a1!y;
                write b1!y;
                """,
                List.of("10", "3", "7", "20", "100"));
    }
}
