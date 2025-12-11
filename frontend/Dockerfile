FROM node:lts AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:lts
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/package.json ./package.json
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/astro.config.mjs ./astro.config.mjs

EXPOSE 4321
CMD ["npm", "run", "preview", "--", "--host"]
