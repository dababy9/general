package si413.spl;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex4Test extends SPLTestBase {
    @Test
    void employees() {
        runSpli("""
                new Employee := class {
                  new paycheck := -1;
                  new yearly := lambda hoursPerWeek {
                    ret := paycheck@hoursPerWeek * 52;
                  };
                };
                new Salaried := class Employee {
                  new salary := 0;
                  new set_salary := lambda s { salary := s; };
                  paycheck := lambda hours {
                    ret := salary / 52;
                  };
                };
                new Hourly := class Employee {
                  new wage := 0;
                  new set_wage := lambda w { wage := w; };
                  paycheck := lambda hours {
                    ret := hours * wage;
                  };
                };
                new manager := Salaried! ;
                manager!set_salary @ 100000;
                write manager!yearly @ 40;
                new consultant := Hourly! ;
                consultant!set_wage @ 100;
                write consultant!yearly @ 15;
                """,
                List.of("99996", "78000"));
    }

    @Test
    void funkyInheritance() {
        runSpli("""
                new A := class {
                  new x := 10;
                };
                new funky := lambda y {
                  new B := class A {
                    # NOTE: x should be the x from the parent class A
                    # and y should be referring to the function argument (outer scope)
                    x := y * 100;
                  };
                  ret := B!;
                };
                write (funky@4)!x; # should be 400
                """,
                List.of("400"));
    }

    @Test
    void bstInheritance() {
        runSpli("""
                new NodeBase := class {
                  new height := -1; # height of an empty tree is defined as -1
                  new insert := -1;
                  new contains := -1;
                  new traverse := -1;
                };
                new Empty := class NodeBase {
                  insert := lambda k {
                    ret := make_leaf@k;
                  };
                  contains := lambda k {
                    ret := false;
                  };
                  traverse := class { };
                };
                new max := lambda a { ret := lambda b {
                  ifelse a >= b { ret := a; } { ret := b; }
                }; };
                new make_internal := lambda key { ret := lambda left { ret := lambda right {
                  ret := class NodeBase {
                    height := max@(left!height)@(right!height) + 1;
                    insert := lambda newkey {
                      ifelse newkey < key {
                        ret := make_internal @ key @ (left!insert @ newkey) @ right;
                      } {
                        ret := make_internal @ key @ left @ (right!insert @ newkey);
                      }
                      height := max@(left!height)@(right!height) + 1;
                    };
                    contains := lambda searchkey {
                      ifelse searchkey = key { ret := true; }
                      {
                        ifelse searchkey < key
                          { ret := left!contains @ searchkey; }
                          { ret := right!contains @ searchkey; }
                      }
                    };
                    traverse := class {
                      left!traverse!;
                      write key;
                      right!traverse!;
                    };
                  }!;
                };};};
                new make_leaf := lambda key {
                  ret := make_internal @ key @ Empty! @ Empty!;
                };
                new BST := class {
                  new root := Empty!;
                  new size := 0;
                  new height := -1;
                  new insert := lambda k {
                    if not root!contains @ k {
                      root := root!insert @ k;
                      size := size + 1;
                      height := root!height;
                    }
                  };
                  new contains := lambda k {
                    ret := root!contains@k;
                  };
                  new print := class {
                    "BEGIN"
                    (root!traverse)!;
                    "END"
                  };
                };
                new t1 := BST!;
                t1!insert@30;
                t1!insert@80;
                t1!insert@90;
                t1!insert@20;
                t1!insert@40;
                t1!insert@70;
                t1!insert@10;
                t1!insert@100;
                t1!insert@90;
                t1!insert@60;
                t1!insert@50;
                t1!print!;
                write t1!size;
                write t1!contains@10;
                write t1!contains@15;
                write t1!height;
                """,
                List.of("BEGIN",
                    "10", "20", "30", "40", "50", "60", "70", "80", "90", "100",
                    "END",
                    "10", // size
                    getTrue(), getFalse(), // contains 10 and 15
                    "5" // height
                    ));
    }

}
