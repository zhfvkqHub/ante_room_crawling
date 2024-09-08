<template>
  <header class="header">
    <div class="header-content">
      <img src="@/assets/house.jpg" alt="Logo" class="logo" />
      <h1 class="site-title">{{ title }}</h1>
      <nav class="navigation">
        <ul>
          <li v-for="(item, index) in navItems" :key="index">
            <a :href="item.link">{{ item.name }}</a>
          </li>
        </ul>
      </nav>
    </div>
  </header>
</template>

<script>
export default {
  name: 'AppHeader',
  props: {
    title: {
      type: String,
      default: '청년안심주택 모집공고 수집'
    },
    navItems: {
      type: Array,
      default: () => [
        // todo : navItems
      ]
    }
  }
}
</script>

<style scoped>
.header {
  width: 100%;
  background-color: #1f577e;
  color: white;
  padding: 15px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  height: 40px;
  margin-right: 20px;
}

.site-title {
  font-family: "Hi Melody", sans-serif;
  font-weight: 400;
  font-style: normal;
  flex-grow: 1;
  font-size: 1.8rem;
  text-align: left;
  color: #ECF0F1;
}

.navigation ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
}

.navigation li {
  margin-left: 15px;
}

.navigation a {
  color: #ECF0F1;
  text-decoration: none;
  font-weight: 500;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.navigation a:hover {
  color: #1ABC9C;
}
</style>
