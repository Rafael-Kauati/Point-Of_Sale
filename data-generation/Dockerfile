FROM python:3.8-slim

# Copie o script Python para o diretório de trabalho
COPY main.py producer.py

# Instale as dependências necessárias
RUN pip install confluent-kafka

# Execute o script Python
CMD ["python", "producer.py"]
