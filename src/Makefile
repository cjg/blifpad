SOURCES = $(shell find . -name "*.java")
CLASSES = $(SOURCES:.java=.class)

PROPERTIES = \
	MyResources.properties\
	MyResources_it.properties

EXTRA = Makefile 

all: $(CLASSES)
	ln -sf ../images
	ln -sf $(foreach p, $(PROPERTIES), ../properties/$(p)) .
# 	(shell for p in $(PROPERTIES); do
# 		echo $p
# 	done)

clean: $(CLASSES:.class=.class-clean)

%.class: %.java
	javac -g -deprecation $<

%.class-clean: %.class
	rm -f $<

# jar-prepare:
# 	-rm -rf .jar.d
# 	mkdir .jar.d
# 	(cd .jar.d; mkdir images)	
# #	find . -name "*.class" -exec cp {} .jar.d/{} \;
# 	cp *.class .jar.d
# 	cp $(PROPERTIES) .jar.d
# #$(shell find images -type d -exec mkdir -p .jar.d/{} \;)
# 	cp $(foreach i, $(IMAGES), images/$(i)) .jar.d/images

# BlueLan.jar: $(CLASSES) jar-prepare
# 	(cd .jar.d; jar cvfm ../$@ ../Manifest *.properties *.class images)
# 	#rm -rf .jar.d

# dist-prepare:
# 	mkdir -p dists
# 	rm -rf dists/$(PACKAGE)$(VERSION)
# 	mkdir -p dists/$(PACKAGE)$(VERSION)
# #mkdir -p $(foreach i, $(shell find $(PACKAGE) -type d), dists/$(PACKAGE)$(VERSION)/$(i))
# 	mkdir -p dists/$(PACKAGE)$(VERSION)/images $(foreach i, $(shell find images -type d), dists/$(PACKAGE)$(VERSION)/$(i))

# %.java-dist: %.java
# 	cp $< dists/$(PACKAGE)$(VERSION)/$(shell dirname $<)

# %-image-dist:
# 	cp images/$(@:-image-dist=) dists/$(PACKAGE)$(VERSION)/images/$(@:-image-dist=) 

# dist: dist-prepare $(SOURCES:.java=.java-dist) $(IMAGES:=-image-dist)
# 	cp $(EXTRA) dists/$(PACKAGE)$(VERSION)
# 	(cd dists; tar cfz $(PACKAGE)$(VERSION).tar.gz $(PACKAGE)$(VERSION))
# 	rm -rf dists/$(PACKAGE)$(VERSION)
