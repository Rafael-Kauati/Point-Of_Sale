# Estágio de construção
FROM node:alpine as builder
WORKDIR '/app'
COPY package.json .
COPY . .
RUN npm install
RUN npm run build

# Estágio de produção
FROM nginx:alpine
WORKDIR /usr/share/nginx/html
COPY --from=builder /app/build .
COPY default.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
