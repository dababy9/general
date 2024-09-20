# SI413 Fall 2023
# Makefile for Lab 4: Recursive Descent Pat parser

MAIN = PatInterpreter

compile: $(MAIN).class

run: run_$(MAIN)

run_%:
	@$(MAKE) -s $*.class && java -ea $*

%.class: $(wildcard *.java)
	javac -Xlint:all -d . *.java

clean:
	rm -f *.class

.PHONY: compile run clean
