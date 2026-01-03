PROJECT_DIR := $(shell realpath $(CURDIR))
PROJECT_NAME := $(shell basename "$(PWD)" | tr '[:upper:]' '[:lower:]')
TEX := presentation.tex
PDF := presentation.pdf

VERSION ?= $(shell git rev-parse --short HEAD)

.DEFAULT_GOAL := help

## build: Compila la presentazione con pdflatex
build:
	$(info ---> Building ${TEX} in dir: ${PROJECT_NAME})
	pdflatex -shell-escape --interaction=nonstopmode --halt-on-error --file-line-error ${TEX}

## version: Mostra la versione corrente (commit short SHA)
version:
	@echo $(VERSION)

## clean: Rimuove tutti i file generati dalla compilazione
clean:
	$(info ---> Cleaning directory ${PROJECT_NAME})
	@rm -r -f \
		*.aux */*.aux \
		*.out *.log *.toc *.idx *.ilg *.ind *.lof *.lol *.gz \
		*.nav *.snm *.vrb *.bak~

.PHONY: help
help: Makefile
	@echo " Choose a command to run in "$(PROJECT_NAME)":"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' | sed -e 's/^/ /'
	@echo
