# Import necessary libraries
from confluent_kafka import Producer
import random
import time

# Kafka configuration
kafka_bootstrap_servers = 'kafka:9092'

# List of products
products = ['bananas', 'apples', 'milk', 'bread', 'eggs', 'cheese', 'chocolate', 'coffee', 'juice', 'yogurt']

# Kafka producer configuration
producer_config = {
    'bootstrap.servers': kafka_bootstrap_servers,
}

# Callback function for message delivery report
def delivery_report(err, msg):
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

# Function to generate a random price
def generate_random_price():
    return round(random.uniform(1.0, 10.0), 2)

# Function to generate a random product from the list
def generate_random_product():
    return random.choice(products)

# Main function
def main():
    # Create a Kafka producer instance with the specified configuration
    producer = Producer(producer_config)

    try:
        # Infinite loop for generating and sending messages
        while True:
            # Generate a random product and price
            product = generate_random_product()
            price = generate_random_price()

            # Prepare message in JSON format
            message = f'{{"product": "{product}", "price": {price}}}'

            # Send message to Kafka topic
            producer.produce('point_of_sale_dg', key=product.encode('utf-8'), value=message.encode('utf-8'), callback=delivery_report)

            # Wait for a random interval before sending the next message
            time.sleep(random.uniform(1, 5))

    except KeyboardInterrupt:
        pass
    finally:
        # Close the producer and flush any outstanding messages
        producer.flush()

# Entry point to the script, execute the main function if this script is run directly
if __name__ == '__main__':
    main()
