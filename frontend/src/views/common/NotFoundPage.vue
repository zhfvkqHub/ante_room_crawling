<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1>404</h1>
      <p>Oops! The page you're looking for doesn't exist.</p>
      <router-link to="/" class="home-link">Go back to Home</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFoundPage'
}
</script>

<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f4f4f4;
  color: #333;
  text-align: center;
  font-family: Arial, sans-serif;
}

.not-found-content {
  max-width: 600px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.not-found h1 {
  font-size: 5rem;
  margin-bottom: 1rem;
  color: #1f577e;
}

.not-found p {
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}

.home-link {
  text-decoration: none;
  color: white;
  background-color: #1f577e;
  padding: 10px 20px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.home-link:hover {
  background-color: #1abc9c;
}
</style>
