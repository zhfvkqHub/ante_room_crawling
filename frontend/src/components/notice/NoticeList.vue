<template>
  <div>
    <div class="search-bar">
      <select v-model="selectedSiteName" @change="fetchNotices" class="filter-select">
        <option value="">전체 사이트</option>
        <option v-for="site in siteOptions" :key="site.value" :value="site.value">
          {{ site.name }}
        </option>
      </select>
      <select v-model="selectedConstituency" @change="fetchNotices" class="filter-select">
        <option value="">전체 지역구</option>
        <option v-for="constituency in constituencyOptions" :key="constituency.value" :value="constituency.value">
          {{ constituency.name }}
        </option>
      </select>
      <select v-model="selectedNoticeType" @change="fetchNotices" class="filter-select">
        <option value="">전체 공지</option>
        <option v-for="noticeType in noticeTypeOptions" :key="noticeType.value" :value="noticeType.value">
          {{ noticeType.name }}
        </option>
      </select>
      <input
          v-model="searchKeyword"
          @keyup.enter="fetchNotices"
          type="text"
          placeholder="제목 검색"
          class="search-input"
      />
      <button @click="fetchNotices" class="search-button">검색</button>
    </div>

    <div class="last-crawled-time">
      마지막 업데이트: {{ lastCrawledTime || '진행 중...' }}
    </div>

    <table class="notice-table">
      <thead>
      <tr>
        <th width="8%">번호</th>
        <th width="18%">사이트명</th>
        <th width="15%">지역</th>
        <th width="43%">제목</th>
        <th width="17%">게시일</th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="(notice, index) in notices"
          :key="notice.id"
          :class="{'highlight-today': isToday(notice.publishedDate)}"
      >
      <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
        <td
            @click="goToSite(notice.siteUrl)"
            style="cursor: pointer;"
        >{{ notice.siteName }}</td>
        <td>{{ notice.constituency }}</td>
        <td
            @click="goToSite(notice.siteUrl, notice.link)"
            style="cursor: pointer;"
        >{{ notice.title }}</td>
        <td>{{ notice.publishedDate }}</td>
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
import {format} from 'date-fns';
import {axiosGetConstituencies, axiosGetLastCrawlingTime, axiosGetNotice, axiosGetSites} from '@/api';

export default {
  data() {
    return {
      notices: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: 15,
      searchKeyword: '',
      selectedSiteName: '',
      selectedConstituency: '',
      selectedNoticeType: '',
      lastCrawledTime: '',
      siteOptions: [],
      constituencyOptions: [],
      noticeTypeOptions: [
        { name: "모집공고(마감포함)", value: "NOTICE" },
        { name: "접수현황", value: "RECEIPT" },
        { name: "당첨자발표", value: "RESULT" },
        { name: "기타", value: "ETC" }
      ]

    }
  },
  mounted() {
    this.fetchNotices();
    this.fetchLastCrawledTime();
    this.fetchSiteOptions();
    this.fetchConstituencyOptions();
  },
  methods: {
    async fetchNotices() {
      const params = {
        page: this.currentPage - 1,
        size: this.pageSize,
        siteName: this.selectedSiteName || null,
        constituency: this.selectedConstituency || null,
        notiType: this.selectedNoticeType || null,
        searchType: 'TITLE',
        searchKeyword: this.searchKeyword || null
      };

      const response = await axiosGetNotice(params);
      this.notices = response.data.content;
      this.totalPages = response.data.totalPages;
    },
    async fetchLastCrawledTime() {
      const response = await axiosGetLastCrawlingTime();
      this.lastCrawledTime = this.formatCrawlingTime(response.data.lastCrawlingTime);
    },
    async fetchSiteOptions() {
      const response = await axiosGetSites();
      this.siteOptions = response.data;
    },
    async fetchConstituencyOptions() {
      const response = await axiosGetConstituencies();
      this.constituencyOptions = response.data;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchNotices();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchNotices();
      }
    },
    isToday(dateString) {
      const today = format(new Date(), 'yyyy-MM-dd');
      return dateString === today;
    },
    formatCrawlingTime(time) {
      if (!time) return null;
      return format(new Date(time), 'yyyy년 MM월 dd일 HH:mm');
    },
    goToSite(siteUrl, link) {
      if (link) {
        window.open(link, '_blank');
      }

      if (siteUrl) {
        window.open(siteUrl, '_blank');
      }
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
  background-color: #F39C12 !important;
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

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 40px;
  margin-left: 10px;
  margin-right: 10px;
  flex-wrap: wrap;
}

.last-crawled-time {
  display: flex;
  margin-bottom: 10px;
  margin-right: 10px;
  font-size: 1rem;
  color: #333;
  justify-content: flex-end;
}

.filter-select,
.search-input,
.search-button {
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 10px;
}

.filter-select,
.search-input {
  flex: 1;
  min-width: 150px;
}

.search-input {
  flex-grow: 2;
}

.search-button {
  flex-grow: 0;
  background-color: #2980B9;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  width: auto;
  min-width: 80px;
}

.search-button:hover {
  background-color: #1A5276;
}
</style>
