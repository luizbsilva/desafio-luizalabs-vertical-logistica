# Desafio Backend Pagnet

Esse projeto foi desenvolvido usando para analise tecnica da Vertical de Logistica do Luiza Labs

## Como Executar

- Clonar repositório git:
```
git clone https://github.com/luizbsilva/desafio-luizalabs-vertical-logistica.git
```

## Como Testar

- Upload do arquivo:
```
curl -X POST -F "file=@/path/to/file/data_1.txt" http://localhost:8080/api/order/upload
``` 
- Lista das operações importadas com totalizador por nome da loja:
```
curl http://localhost:8080/api/order
```
