from confluent_kafka import Producer,Consumer, KafkaError
import random
import time
import socket
import json
# kafka configuration
# kafka configuration
kafka_bootstrap_servers = 'kafka:9093'

# Kafka producer configuration
producer_config = {
    'bootstrap.servers': kafka_bootstrap_servers,
        'group.id': 'index2',

}

consumer_config = {
    'bootstrap.servers': kafka_bootstrap_servers,
    'group.id': 'index',
    'auto.offset.reset': 'earliest',  # Adjust this based on your use case
}

#Products


# Callback function for message delivery report
def delivery_report(err, msg):
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

# Function to generate a random price
def generate_random_price(base_price):
    price_change_percentage = random.uniform(0.1, 0.5)  # 10% to 50%

    price_direction = random.choice([-1, 1])  

    price_change = base_price * price_change_percentage * price_direction

    new_price = round(base_price + price_change, 2)
    alert=False
    if abs(price_change_percentage) > 0.4:
        # If it does, update the alert variable
        alert = True
    return new_price,alert

def generate_random_shopping_cart(products_dict):
    shopping_cart = []

    for product, details in random.sample(products_dict.items(), k=random.randint(1, len(products_dict))):
        max_units= int(10)
        if max_units < 1:
            units = 0
        else:
            units = random.randint(1, max_units)
        total_price = round(details['price'] * units, 2)
        shopping_cart.append({
            'product': product,
            'units': units,
            'Price': total_price
        })
    return shopping_cart

# Function to check if Kafka is ready
def is_kafka_readyProducer():
    try:
        # Check if Kafka port is open
        socket.create_connection((kafka_bootstrap_servers.split(":")[0], int(kafka_bootstrap_servers.split(":")[1])))

        return True
    except (ConnectionRefusedError, TimeoutError):
        return False

def is_kafka_readyConsumer():
    try:
        consumer = Consumer(consumer_config)
        consumer.subscribe(['IndexID'])
        msg = consumer.poll(1)
        return msg is not None
    except Exception:
        return False


# Main function
def main():
    # Wait for Kafka to be ready
    while not is_kafka_readyConsumer() and is_kafka_readyProducer():
        print("Waiting for Kafka to be ready...")
       #  time.sleep(1)

    producer = Producer(producer_config)
    consumer = Consumer(consumer_config)
    consumer.subscribe(['IndexID'])

    try:
        timeCounter = 0
        timeLimit = 5  # Seconds
        # Infinite loop for generating and sending messages
        while True:

          msg = consumer.poll(500)
          # Generate a random product and price
          if msg is not None:
            received_message = msg.value().decode('utf-8') 
            json_data = json.loads(received_message)


         
            new_dict = {}
            for item in json_data:
                new_dict[item['id']] = {'price': item['price'], 'curr_quantity': item['curr_quantity']}
            cart = generate_random_shopping_cart(new_dict)
            new_dict_json = json.dumps(cart)
            producer.produce('point_of_sale_dg', value=new_dict_json.encode('utf-8'), callback=delivery_report)
            

          # Wait for a random interval before sending the next message
            time.sleep(timeLimit)

    except KeyboardInterrupt:
        pass
    finally:
        # Close the producer and flush any outstanding messages
        producer.flush()

# Entry point to the script, execute the main function if this script is run directly
if __name__ == '__main__':
    main()
