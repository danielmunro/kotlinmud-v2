lint:
	ktlint -F "src/**/*.kt"

run:
	./gradlew clean run

test:
	./gradlew clean test