library("maxent")
library("caret")
library("class")
library("e1071")
library("randomForest")

##################
# FUNCOES GERAIS #
##################

# Diretorio base dos arquivos usados
base_folder <- "/tmp/"

# Concatena os parametros
concat = function(...) {
  paste(..., sep = "")
}

# Escreve texto na saida padrao, com uma quebra de linha ao fim
log <- function(...) {
  cat(concat(..., "\n"))
}

# Le um arquivo e transpoe a matriz para formar um dado de 4096 dimensoes
read_file <- function(file) {
  return (read.table(file, header = FALSE, row.names = NULL, sep = ","))
}

# Calcula a acuracia da analise, pelo resultado obtido e o esperado
calculate_accuracy <- function(result_classes, expected_classes) {
  return (sum(expected_classes == result_classes) / length(expected_classes))
}

###################
# CLASSIFICADORES #
###################

maximum_entropy_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  c <- hiperparameter

  classificator <- maxent(train_data, train_classes)
  result_classes <- predict(classificator, test_data)
  accuracy <- calculate_accuracy(result_classes, test_classes)

  return (accuracy)
}

naive_bayes_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  c <- hiperparameter

  classificator <- naiveBayes(train_data, factor(train_classes))
  result_classes <- predict(classificator, test_data)
  accuracy <- calculate_accuracy(result_classes, test_classes)

  return (accuracy)
}

###################
# HIPERPARAMETROS #
###################

maximum_entropy_hiperparameters <- function(data, expected_classes) {
  #return (c(0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000))
  return (c(1))
}

naive_bayes_hiperparameters <- function(data, expected_classes) {
  #return (c(0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000))
  return (c(1))
}

###########################
# EXECUCAO DO EXPERIMENTO #
###########################

log(" >  Lendo dados de treino...")
log("")
file = concat(base_folder, "train_result_file.txt")
train_data = read_file(file)
train_classes = train_data[c(4389)]
train_classes = train_classes[,1]
train_data = train_data[c(1:4388)]


log(" >  Lendo dados de teste...")
log("")
file = concat(base_folder, "test_result_file.txt")
test_data = read_file(file)
test_classes = test_data[c(4389)]
test_classes = test_classes[,1]
test_data = test_data[c(1:4388)]

log(" >  Executando Maximum Entropy...")
accuracy <- maximum_entropy_classificator(train_data, test_data, train_classes, test_classes, 3)
log("Acuracia: ", accuracy)

log(" >  Executando Naive Bayes...")
accuracy <- naive_bayes_classificator(train_data, test_data, train_classes, test_classes, 3)
log("Acuracia: ", accuracy)