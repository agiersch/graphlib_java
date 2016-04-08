
SRC = DrawingWindow.java $(wildcard *.java)
CLASS = $(SRC:%.java=%.class)

.PHONY: all clean

all: DrawingWindow.java
	$(MAKE) -f ../Makefile.generic $(CLASS)

clean:
	$(RM) *~
	$(RM) DrawingWindow.java
	$(RM) *.class

DrawingWindow.java:
	ln -s ../DrawingWindow.java .
