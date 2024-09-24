ANTLR_JAR = antlr.jar

all: Calc.class calc.sh

$(ANTLR_JAR):
	curl -sL -o $@ 'https://www.antlr.org/download/antlr-4.13.1-complete.jar'

ACalcParser.java: $(ANTLR_JAR) $(wildcard *.g4)
	java -cp $(ANTLR_JAR) org.antlr.v4.Tool *.g4

Calc.class: $(ANTLR_JAR) $(wildcard *.java) ACalcParser.java
	javac -Xlint:all -cp .:$(ANTLR_JAR) *.java

calc.sh:
	@echo "Creating $@ bash script..."
	@echo "#!/usr/bin/env bash" >$@ ; \
	echo "set -e" >>$@ ; \
	echo "$(MAKE) -s -f \"$(firstword $(MAKEFILE_LIST))\" Calc.class" >>$@ ; \
	echo 'java -ea -cp ".:$(ANTLR_JAR)" Calc "$$@"' >>$@ ; \
        chmod +x "$@"

clean:
	rm -f *.class *.tokens *.interp ACalc*.java calc.sh

pristine: clean
	rm -f $(ANTLR_JAR)

.INTERMEDIATE:
.SECONDARY: $(ANTLR_JAR)
.PHONY: clean all pristine
