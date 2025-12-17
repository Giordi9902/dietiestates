PROJECT_DIR := $(shell realpath $(CURDIR))
PROJECT_NAME := $(shell basename "$(PWD)" | tr '[:upper:]' '[:lower:]')
NAME = $(patsubst %.tex,%.pdf,$(wildcard *.tex))

VERSION ?= $(shell git rev-parse --short HEAD)

.DEFAULT_GOAL := help

## build: Build the project
build:
	$(info ---> Building document ${NAME} in dir: ${PROJECT_NAME})
	lualatex -shell-escape --interaction=nonstopmode --halt-on-error --file-line-error main.tex

## version: Display the current version for the document
version:
	@echo $(VERSION)


## clean: Remove generated files
clean:
	$(info ---> Cleaning directory ${PROJECT_NAME})
	@rm -r -f *.aux */*.aux *.lol *.out *.log *.lof *.ilg *.ind *.gz *.toc *.idx *.bak~

.PHONY: help
help: Makefile
	@echo " Choose a command to run in "$(PROJECT_NAME)":"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' | sed -e 's/^/ /'
	@echo
