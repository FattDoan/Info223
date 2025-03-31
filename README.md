# Momies et Pyramides

> Ce README n'est pas un rapport. Il sert simplement de note de synthèse pour le développement mais il sera la partie principale du rapport qui sera au format **PDF** en français.

## Implemented features

- **Draw calls optimization**: no more seperate vertices draw (i.e batch all the vertices into shapes and send to the GPU all together)

- **Frustum culling**: combined with predefined PShape from draw calls optimization -> lightning fast performances (achieving 250+ fps even with Fragment Shading on 1080p)

- **Texture, Lighting and Shaders**: implement both Vertex Shading and Fragment Shading (prefer Fragment Shading for more realistic lighting)

- **Near O(1) collision detection**: thanks to static environment

### TODO

- [ ] Touching up the pyramid appearance (texture for floor, roof and the outside)

- [ ] Add map and compass

- [ ] Add sun lighting and dessert vibe outside pyramid

- [ ] Add NPC

## Build system

Instead of using Processing rather limited built-in IDE, we build the project by importing necessary libraries required by Processing and using a robust and industry-standard Java build system **Maven**. 

(JetBrain IDEs would also have sufficed but I hate using them)

Processing comes with its **core** library ([Maven Repository: org.processing » core](https://mvnrepository.com/artifact/org.processing/core)) which depends on 2 other libraries for Java bindings: **jogl-all** and **gluegen-rt**. 

[Maven Repository: org.jogamp.jogl » jogl-all](https://mvnrepository.com/artifact/org.jogamp.jogl/jogl-all)

[Maven Repository: org.jogamp.gluegen » gluegen-rt](https://mvnrepository.com/artifact/org.jogamp.gluegen/gluegen-rt)

We can manage these dependencies within our **pom.xml** file. 

Reference: [Maven - JogampWiki](https://jogamp.org/wiki/index.php?title=Maven)

#### Notes:

1. We use the stable version of **core** (3.3.7) and not the lastest version as to avoid fatal bugs (that I have encountered during the attempt of installing one) and simplify our dependencies (newer version of **jogamp** is buggy and requires a lot more dependencies)

2. This build only (supposedly) works on **Linux**. Different platforms require different dependencies ([Reference]([Downloading and installing JOGL - JogampWiki](https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL))) and I have just only tested building on linux-amd64. Obtaining platform independent build requires a tad more efforts in tweaking Maven configuration which is not our priority.

## Compile and run

> **Make sure Maven installed on your system before**

If haven't done once, download all dependencies for Processing.

```bash
mvn clean install -U
```

Compile the project each time we've modified our codes

```bash
mvn clean compile
```

Then run

```bash
mvn exec:java
```
