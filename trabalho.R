# MC906 - Trabalho 3
# Aluno: Paulo Vitor Martins do Rego
# RA: 118343

library("caret")
library("class")
library("e1071")
library("randomForest")

##################
# FUNCOES GERAIS #
##################

# Diretorio base dos arquivos usados
base_folder <- "/tmp/trabalho_final/"

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

random_forest_classificator <- function(train_data, test_data, train_classes, test_classes, hiperparameter) {
  mtry <- hiperparameter

  classificator <- randomForest(x = train_data, y = train_classes, mtry = mtry)
  result_classes <- predict(classificator, test_data)
  accuracy <- calculate_accuracy(result_classes, test_classes)

  return (accuracy)
}

###################
# HIPERPARAMETROS #
###################

linear_svm_hiperparameters <- function(data, expected_classes) {
  return (c(0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000))
}

rbf_svm_hiperparameters <- function() {
  elements <- c(0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000)
  combinations = t(expand.grid(elements, elements))
  tuples = vector(mode = "list", length = 64)
  
  for (i in 1:64) {
    tuples[[i]] = c(combinations[,i][1], combinations[,i][2])
  }

  return (tuples)
}

knn_hiperparameters <- function(train_data, test_data, train_classes, test_classes, k) {
  return (c(1, 3, 5, 11, 21, 31))
}

random_forest_hiperparameters <- function(data, expected_classes) {
  return (c(2, 3, 5, 10, 20, 40, 60))
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
    max_accuracy <- 0

    log("")
    log(" >  >  >  >  Calculando acuracia media do k-fold externo...")
    log("")

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

    log("")
    log(" >  >  >  >  A melhor acuracia foi obtida com o(s) hiperparametro(s) ", paste(max_hiperparameter, collapse = " e "), ", e vale ", max_accuracy)

    top_accuracy <- classificator(train_data, test_data, train_classes, test_classes, max_hiperparameter)
    average_accuracy <- average_accuracy + top_accuracy

    log("")
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

  log(" >  >  ############################")
  log(" >  >  # ALGORITMO: RANDOM FOREST #")
  log(" >  >  ############################")
  run_classification(data, expected_classes, random_forest_hiperparameters, random_forest_classificator)
}

###########################
# EXECUCAO DO EXPERIMENTO #
###########################

log(" >  Lendo dados...")
log("")
file = concat(base_folder, "train_result_file.txt")
data = read_file(file)
expected_classes = data[c(5831)]
data = data[c(1:5830)]

log(" >  Executando PCA do dados...")
log("")
pca = prcomp(data)

log(" >  Resultado do PCA:")
log("")
print(summary(pca))
log("")

log(" >  Reduzindo a dimensionalidade dos dados para 7 dimensoes...")
log("")
reduced_data = pca$x[,1:7]

#log(" >  #################################################")
#log(" >  # Rodando o experimento para os dados originais #")
#log(" >  #################################################")
#log("")
#run_experiment(data, expected_classes)

log(" >  #####################################################################")
log(" >  # Rodando o experimento para os dados com dimensionalidade reduzida #")
log(" >  #####################################################################")
log("")
run_experiment(reduced_data, expected_classes)

