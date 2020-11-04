FROM openjdk:11
WORKDIR /app/
COPY ./* ./
RUN javac Operator_Precedence.java