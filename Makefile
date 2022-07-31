lint:
	ktlint -F "src/**/*.kt"

run:
	./gradlew clean run

test:
	./gradlew clean test

migrate:
	./gradlew clean run --args='migrate'