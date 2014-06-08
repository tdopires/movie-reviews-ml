# MC906 - Trabalho 3
# Aluno: Paulo Vitor Martins do Rego
# RA: 118343

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

linear_svm_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  c <- hiperparameter

  classificator <- svm(x = train_data, y = train_classes, type = 'C', kernel = 'linear', cost = c)
  result_classes <- predict(classificator, test_data)
  accuracy <- calculate_accuracy(result_classes, test_classes)

  return (accuracy)
}

rbf_svm_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  c <- hiperparameter[1]
  gamma <- hiperparameter[2]

  classificator <- svm(x = train_data, y = train_classes, type = 'C', kernel = 'radial', cost = c, gamma = gamma)
  result_classes <- predict(classificator, test_data)
  accuracy <- calculate_accuracy(result_classes, test_classes)

  return (accuracy)
}

knn_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  k <- hiperparameter

  result_classes <- knn(train_data, test_data, train_classes, k = k)
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

linear_svm_hiperparameters <- function(data, expected_classes) {
  return (c(0.001, 0.01, 0.1, 1, 10, 100))#, 1000, 10000))
  #return (c(1))
}

rbf_svm_hiperparameters <- function() {
  elements <- c(0.001, 0.01, 0.1, 1, 10, 100)#, 1000, 10000)
  #elements <- c(1)
  combinations = t(expand.grid(elements, elements))
  tuples = vector(mode = "list", length = 36)
  #tuples = vector(mode = "list", length = 1)
  
  for (i in 1:36) {
    tuples[[i]] = c(combinations[,i][1], combinations[,i][2])
  }

  return (tuples)
}

knn_hiperparameters <- function(train_data, test_data, train_classes, test_classes, k) {
  return (c(1, 3, 5, 11, 21, 31))
  #return (c(1))
}

##########################
# FUNCOES DO EXPERIMENTO #
##########################

run_classification <- function(data, expected_classes, hiperparameters, classificator, ke = 5, ki = 5) {

  # Dividindo os dados em k-folds
  data_partitions <- createFolds(t(expected_classes), ke, list = TRUE, returnTrain = FALSE)
  average_accuracy <- 0

  log("")
  log(" >  >  >  Calculando acuracia media do algoritmo...")

  for (data_partition in data_partitions) {
    train_data <- data[-data_partition, ]
    test_data <- data[data_partition, ]
    train_classes <- expected_classes[-data_partition, ]
    test_classes <- expected_classes[data_partition, ]
    max_accuracy <- -1

    log("")
    log(" >  >  >  >  Calculando acuracia media do k-fold externo...")

    for (hiperparameter in hiperparameters()) {
      data_partitions2 <- createFolds(t(train_classes), ki, list = TRUE, returnTrain = FALSE)
      accuracy <- 0

      for (data_partition2 in data_partitions2) {
        train_data2 <- data[-data_partition2, ]
        test_data2 <- data[data_partition2, ]
        train_classes2 <- expected_classes[-data_partition2, ]
        test_classes2 <- expected_classes[data_partition2, ]

        accuracy <- accuracy + classificator(train_data2, test_data2, train_classes2, test_classes2, hiperparameter)
      }

      accuracy <- accuracy / ki
      
      if (accuracy > max_accuracy) {
        max_accuracy <- accuracy
        max_hiperparameter <- hiperparameter
      }

      log(" >  >  >  >  >  Para o(s) hiperparametro(s) ", paste(hiperparameter, collapse = " e "), ", a acuracia media foi de ", accuracy)
    }

    log(" >  >  >  >  A melhor acuracia foi obtida com o(s) hiperparametro(s) ", paste(max_hiperparameter, collapse = " e "), ", e vale ", max_accuracy)

    top_accuracy <- classificator(train_data, test_data, train_classes, test_classes, max_hiperparameter)
    average_accuracy <- average_accuracy + top_accuracy

    log(" >  >  >  >  A acuracia de todo o conjunto obtida com o(s) hiperparametro(s) ", paste(max_hiperparameter, collapse = " e "), " vale ", top_accuracy)
  }
  
  average_accuracy <- average_accuracy / ke

  log("")
  log(" >  >  >  A acuracia media do algoritmo foi de ", average_accuracy)
  log("")
  log("")

  return(average_accuracy)
}

run_experiment <- function(data, expected_classes) {
  log(" >  >  ##############################")
  log(" >  >  # ALGORITMO: MAXIMUM ENTROPY #")
  log(" >  >  ##############################")
  run_classification(data, expected_classes, maximum_entropy_hiperparameters, maximum_entropy_classificator)

  log(" >  >  ##########################")
  log(" >  >  # ALGORITMO: NAIVE BAYES #")
  log(" >  >  ##########################")
  run_classification(data, expected_classes, naive_bayes_hiperparameters, naive_bayes_classificator)

  log(" >  >  #########################")
  log(" >  >  # ALGORITMO: SVM LINEAR #")
  log(" >  >  #########################")
  run_classification(data, expected_classes, linear_svm_hiperparameters, linear_svm_classificator)

  log(" >  >  ######################")
  log(" >  >  # ALGORITMO: SVM RBF #")
  log(" >  >  ######################")
  run_classification(data, expected_classes, rbf_svm_hiperparameters, rbf_svm_classificator)

  log(" >  >  #########################")
  log(" >  >  # ALGORITMO: K VIZINHOS #")
  log(" >  >  #########################")
  run_classification(data, expected_classes, knn_hiperparameters, knn_classificator)
}

###########################
# EXECUCAO DO EXPERIMENTO #
###########################

log(" >  Lendo dados...")
log("")
file = concat(base_folder, "train_result_file.txt")
data = read_file(file)
expected_classes = data[c(6)]
data = data[c(1:5)]

#log(" >  Executando PCA do dados...")
#log("")
#pca = prcomp(data)

#log(" >  Resultado do PCA:")
#log("")
#print(summary(pca))
#log("")

#log(" >  Reduzindo a dimensionalidade dos dados para 7 dimensoes...")
#log("")
#reduced_data = pca$x[,1:270]

log(" >  #################################################")
log(" >  # Rodando o experimento para os dados originais #")
log(" >  #################################################")
log("")
run_experiment(data, expected_classes)

#log(" >  #####################################################################")
#log(" >  # Rodando o experimento para os dados com dimensionalidade reduzida #")
#log(" >  #####################################################################")
#log("")
#run_experiment(reduced_data, expected_classes)

