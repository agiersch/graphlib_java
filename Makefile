include ../subdefs.mk

DEST = $(HOME)/public_html/enseignement/Permanent/AlgoProg/tp/graph

SRC = DrawingWindow.java
EXAMPLES = Hello.java Exemple1.java Exemple2.java Exemple3.java

EXAMPLES_CLASS = $(EXAMPLES:%.java=%.class)

all:
	$(MAKE) -f ../Makefile.generic $(EXAMPLES_CLASS)
	$(RM) -r html
	javadoc -author -version -notree -nodeprecated -nohelp \
		-encoding utf-8 -docencoding utf-8 -charset utf-8 \
		-d html/ $(SRC)

clean:
	$(RM) DrawingWindow*.class
	$(RM) $(EXAMPLES_CLASS)
	$(RM) -r html/

realclean: clean

install: all
	$(RM) -r $(DEST)
	$(INSTALL) -C -m 755 -d $(DEST) $(DEST)/resources
	$(INSTALL) -C -m 644 $(SRC) $(EXAMPLES) $(DEST)
	$(INSTALL) -C -m 644 html/resources/* $(DEST)/resources
	$(INSTALL) -C -m 644 html/*[.-]* $(DEST)
