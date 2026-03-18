<template>
  <footer class="footer">
    <div class="footer-content">
      <p>월-금 08:00~19:00 / 30분 간격 자동 크롤링</p>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>

<style scoped>
.footer {
  width: 100%;
  background-color: #1f577e;
  color: rgba(255, 255, 255, 0.8);
  padding: 16px 0;
  box-shadow: 0 -1px 4px rgba(0, 0, 0, 0.08);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
  font-size: 0.85rem;
}

.footer-content p {
  margin: 0;
}

@media (max-width: 768px) {
  .footer {
    padding: 12px 0;
  }

  .footer-content {
    font-size: 0.8rem;
  }
}
</style>
