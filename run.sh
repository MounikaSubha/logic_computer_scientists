echo "Enter the number of queens: ":
read value
java NQueens.java $value
minisat input$value.cnf output
cat output