<template>
  <div>
    <table class="notice-table">
      <thead>
      <tr>
        <th width="10%">번호</th>
        <th width="15%">사이트명</th>
        <th width="50%">제목</th>
        <th width="15%">게시일</th>
        <th width="10%">사이트</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(notice, index) in notices" :key="notice.id" :class="{'highlight-today': isToday(notice.publishedDate)}">
        <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
        <td>{{ notice.siteName }}</td>
        <td>{{ notice.title }}</td>
        <td>{{ notice.publishedDate }}</td>
        <td><a :href="notice.siteUrl" target="_blank" class="notice-link">보기</a></td>
      </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">이전</button>
      <span>페이지 {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
    </div>
  </div>
</template>

<script>
import { format } from 'date-fns';

export default {
  props: {
    notices: {
      type: Array,
      required: true
    },
    currentPage: {
      type: Number,
      required: true
    },
    pageSize: {
      type: Number,
      required: true
    },
    totalPages: {
      type: Number,
      required: true
    }
  },
  emits: ['prevPage', 'nextPage'],
  methods: {
    isToday(dateString) {
      const today = format(new Date(), 'yyyy-MM-dd');
      return dateString === today;
    },
    prevPage() {
      this.$emit('prevPage');
    },
    nextPage() {
      this.$emit('nextPage');
    }
  }
}
</script>

<style scoped>
.notice-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
}

.notice-table th {
  background-color: #2980B9;
  color: #ECF0F1;
  font-weight: bold;
  text-transform: uppercase;
  padding: 15px 20px;
  text-align: left;
}

.notice-table td {
  padding: 15px 20px;
  text-align: left;
  font-size: 1rem;
  border-bottom: 1px solid #ddd;
}

.notice-table tbody tr:nth-child(even) {
  background-color: #f7f7f7;
}

.notice-table tbody tr:nth-child(odd) {
  background-color: #ffffff;
}

.notice-table tbody tr:hover {
  background-color: #f0f0f0;
}

.notice-link {
  color: #2980B9;
  text-decoration: none;
  font-weight: bold;
}

.notice-link:hover {
  text-decoration: underline;
  color: #1A5276;
}

.highlight-today {
  background-color: #F39C12;
  color: #ffffff;
  font-weight: bold;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.pagination button {
  margin: 0 10px;
  padding: 10px 15px;
  font-size: 1rem;
  cursor: pointer;
  border: 1px solid #2980B9;
  background-color: white;
  color: #2980B9;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #2980B9;
  color: white;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination span {
  margin: 0 15px;
  font-size: 1.2rem;
  color: #555;
}
</style>