include ../subdefs.mk

DEST = $(HOME)/public_html/enseignement/Permanent/AlgoProg/tp/graph

SRC = DrawingWindow.java
EXAMPLES = Hello.java Exemple1.java Exemple2.java Exemple3.java

SRC_CLASS = $(SRC:%.java=%.class)
EXAMPLES_CLASS = $(EXAMPLES:%.java=%.class)

all: html/index.html
	$(MAKE) -f ../Makefile.generic $(SRC_CLASS) $(EXAMPLES_CLASS)
	$(MAKE) -f ../Makefile.generic Test.class

html/index.html: $(SRC) overview.html
	$(RM) -r html
	javadoc -locale fr_FR -encoding utf-8 -docencoding utf-8 -charset utf-8 \
		-quiet -notree -nodeprecated -nohelp \
		-author -version -public \
		-overview overview.html \
		-doctitle "Petite bibliothèque graphique" \
		-windowtitle "Petite bibliothèque graphique" \
		-d html/ $(SRC)

clean:
	$(RM) DrawingWindow*.class
	$(RM) Test.class
	$(RM) $(EXAMPLES_CLASS)
	$(RM) -r html/

realclean: clean

install: html/index.html
	$(INSTALL) -C -m 755 -d $(DEST) $(DEST)/resources
	$(INSTALL) -C -m 644 $(SRC) $(EXAMPLES) $(DEST)
	$(INSTALL) -C -m 644 html/resources/* $(DEST)/resources
	$(INSTALL) -C -m 644 html/*[.-]* $(DEST)
