# Exemplu de Makefile pentru soluții scrise în Java.

.PHONY: build clean

build: P1.class P2.class P3.class P4.class P5.class P6.class P7.class P8.class P9.class

run-p1:
	java Feribot
run-p2:
	java Nostory
run-p3:
	java Sushi
run-p4:
	java Semnale
run-p5:
	java Badgpt
run-p6:
	java Supercomputer
run-p7:
	java Ferate
run-p8:
	java Teleportare
run-p9:
	java Magazin


# Nu uitați să modificați numele surselor.
P1.class: Feribot.java
	javac $^
P2.class: Nostory.java
	javac $^
P3.class: Sushi.java
	javac $^
P4.class: Semnale.java
	javac $^
P5.class: Badgpt.java
	javac $^
P6.class: Supercomputer.java
	javac $^
P7.class: Ferate.java
	javac $^
P8.class: Teleportare.java
	javac $^
P9.class: Magazin.java
	javac $^

# Vom șterge fișierele bytecode compilate.
clean:
	rm -f *.class
