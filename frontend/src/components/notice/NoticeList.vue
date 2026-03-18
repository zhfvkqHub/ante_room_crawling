<template>
  <div>
    <div class="search-bar">
      <select v-model="selectedSiteName" @change="resetAndFetch" class="filter-select">
        <option value="">전체 사이트</option>
        <option v-for="site in siteOptions" :key="site.value" :value="site.value">
          {{ site.name }}
        </option>
      </select>
      <select v-model="selectedConstituency" @change="resetAndFetch" class="filter-select">
        <option value="">전체 지역구</option>
        <option v-for="constituency in constituencyOptions" :key="constituency.value" :value="constituency.value">
          {{ constituency.name }}
        </option>
      </select>
      <select v-model="selectedNoticeType" @change="resetAndFetch" class="filter-select">
        <option value="">전체 공지</option>
        <option v-for="noticeType in noticeTypeOptions" :key="noticeType.value" :value="noticeType.value">
          {{ noticeType.name }}
        </option>
      </select>
      <input
          v-model="searchKeyword"
          @keyup.enter="resetAndFetch"
          type="text"
          placeholder="제목 검색"
          class="search-input"
      />
      <button @click="resetAndFetch" class="search-button">검색</button>
    </div>

    <div class="last-crawled-time">
      마지막 업데이트: {{ lastCrawledTime || '진행 중...' }}
    </div>

    <!-- 데스크탑: 테이블 -->
    <table class="notice-table">
      <thead>
      <tr>
        <th width="6%">번호</th>
        <th width="16%">사이트명</th>
        <th width="10%">지역</th>
        <th width="8%">유형</th>
        <th width="44%">제목</th>
        <th width="16%">게시일</th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="notices.length === 0">
        <td colspan="6" class="empty-row">검색 결과가 없습니다.</td>
      </tr>
      <tr
          v-for="(notice, index) in notices"
          :key="notice.id"
          :class="{'highlight-today': isToday(notice.publishedDate)}"
      >
        <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
        <td
            @click="goToSite(notice.siteUrl)"
            class="clickable"
        >{{ notice.siteName }}</td>
        <td>{{ notice.constituency }}</td>
        <td>
          <span :class="['noti-badge', getBadgeClass(notice.notiType)]">
            {{ getNotiTypeLabel(notice.notiType) }}
          </span>
        </td>
        <td
            @click="goToSite(notice.siteUrl, notice.link)"
            class="clickable title-cell"
        >{{ notice.title }}</td>
        <td>{{ notice.publishedDate }}</td>
      </tr>
      </tbody>
    </table>

    <!-- 모바일: 카드형 -->
    <div class="notice-cards">
      <div v-if="notices.length === 0" class="empty-card">
        검색 결과가 없습니다.
      </div>
      <div
          v-for="(notice) in notices"
          :key="'card-' + notice.id"
          class="notice-card"
          :class="{'highlight-today-card': isToday(notice.publishedDate)}"
          @click="goToSite(notice.siteUrl, notice.link)"
      >
        <div class="card-header">
          <span class="card-site">{{ notice.siteName }}</span>
          <span :class="['noti-badge', getBadgeClass(notice.notiType)]">
            {{ getNotiTypeLabel(notice.notiType) }}
          </span>
        </div>
        <div class="card-title">{{ notice.title }}</div>
        <div class="card-footer">
          <span class="card-constituency">{{ notice.constituency }}</span>
          <span class="card-date">{{ notice.publishedDate }}</span>
        </div>
      </div>
    </div>

    <div class="pagination">
      <button @click="goToPage(1)" :disabled="currentPage === 1" class="page-btn">
        &laquo;
      </button>
      <button @click="prevPage" :disabled="currentPage === 1" class="page-btn">
        &lsaquo;
      </button>
      <button
          v-for="page in displayedPages"
          :key="page"
          @click="goToPage(page)"
          :class="['page-btn', { active: currentPage === page }]"
      >
        {{ page }}
      </button>
      <button @click="nextPage" :disabled="currentPage === totalPages" class="page-btn">
        &rsaquo;
      </button>
      <button @click="goToPage(totalPages)" :disabled="currentPage === totalPages" class="page-btn">
        &raquo;
      </button>
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
  computed: {
    displayedPages() {
      const pages = [];
      const maxVisible = 5;
      let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
      let end = Math.min(this.totalPages, start + maxVisible - 1);

      if (end - start + 1 < maxVisible) {
        start = Math.max(1, end - maxVisible + 1);
      }

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      return pages;
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
      this.totalPages = response.data.totalPages || 1;
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
    resetAndFetch() {
      this.currentPage = 1;
      this.fetchNotices();
    },
    goToPage(page) {
      this.currentPage = page;
      this.fetchNotices();
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
      } else if (siteUrl) {
        window.open(siteUrl, '_blank');
      }
    },
    getNotiTypeLabel(notiType) {
      const labels = {
        'NOTICE': '모집',
        'RECEIPT': '접수',
        'RESULT': '발표',
        'ETC': '기타',
        '모집공고(마감포함)': '모집',
        '접수현황': '접수',
        '당첨자발표': '발표',
        '기타': '기타'
      };
      return labels[notiType] || notiType;
    },
    getBadgeClass(notiType) {
      const classes = {
        'NOTICE': 'badge-notice',
        'RECEIPT': 'badge-receipt',
        'RESULT': 'badge-result',
        'ETC': 'badge-etc',
        '모집공고(마감포함)': 'badge-notice',
        '접수현황': 'badge-receipt',
        '당첨자발표': 'badge-result',
        '기타': 'badge-etc'
      };
      return classes[notiType] || 'badge-etc';
    }
  }
}
</script>

<style scoped>
/* ===== 검색바 ===== */
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  padding: 0 10px;
  flex-wrap: wrap;
}

.last-crawled-time {
  display: flex;
  margin-bottom: 10px;
  padding-right: 10px;
  font-size: 0.9rem;
  color: #888;
  justify-content: flex-end;
}

.filter-select,
.search-input,
.search-button {
  padding: 10px 12px;
  font-size: 0.95rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  outline: none;
  transition: border-color 0.2s;
}

.filter-select:focus,
.search-input:focus {
  border-color: #2980B9;
}

.filter-select,
.search-input {
  flex: 1;
  min-width: 120px;
}

.search-input {
  flex-grow: 2;
}

.search-button {
  flex-grow: 0;
  background-color: #2980B9;
  color: white;
  border: none;
  cursor: pointer;
  min-width: 70px;
  font-weight: 600;
  transition: background-color 0.2s;
}

.search-button:hover {
  background-color: #1A5276;
}

/* ===== 공고 유형 배지 ===== */
.noti-badge {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  white-space: nowrap;
}

.badge-notice {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.badge-receipt {
  background-color: #e3f2fd;
  color: #1565c0;
}

.badge-result {
  background-color: #fff3e0;
  color: #e65100;
}

.badge-etc {
  background-color: #f5f5f5;
  color: #757575;
}

/* ===== 데스크탑 테이블 ===== */
.notice-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.notice-table th {
  background-color: #2980B9;
  color: #ECF0F1;
  font-weight: 600;
  padding: 12px 16px;
  text-align: left;
  font-size: 0.9rem;
}

.notice-table td {
  padding: 12px 16px;
  text-align: left;
  font-size: 0.9rem;
  border-bottom: 1px solid #eee;
}

.notice-table tbody tr:hover {
  background-color: #f8f9fa;
}

.clickable {
  cursor: pointer;
  color: #2980B9;
}

.clickable:hover {
  text-decoration: underline;
}

.title-cell {
  color: #333;
}

.title-cell:hover {
  color: #2980B9;
}

.highlight-today {
  background-color: #fff8e1 !important;
  border-left: 3px solid #F39C12;
}

.empty-row {
  text-align: center !important;
  color: #999;
  padding: 40px 20px !important;
}

/* ===== 모바일 카드 (기본 숨김) ===== */
.notice-cards {
  display: none;
}

.notice-card {
  background: #fff;
  border-radius: 10px;
  padding: 14px 16px;
  margin-bottom: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.notice-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.highlight-today-card {
  border-left: 3px solid #F39C12;
  background-color: #fffde7;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.card-site {
  font-size: 0.8rem;
  color: #2980B9;
  font-weight: 600;
}

.card-title {
  font-size: 0.95rem;
  color: #333;
  line-height: 1.4;
  margin-bottom: 10px;
  word-break: keep-all;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-constituency {
  font-size: 0.8rem;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
}

.card-date {
  font-size: 0.8rem;
  color: #999;
}

.empty-card {
  text-align: center;
  color: #999;
  padding: 40px 20px;
}

/* ===== 페이지네이션 ===== */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 24px;
  gap: 4px;
}

.page-btn {
  min-width: 36px;
  height: 36px;
  padding: 0 8px;
  font-size: 0.9rem;
  cursor: pointer;
  border: 1px solid #ddd;
  background-color: white;
  color: #555;
  border-radius: 6px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-btn:hover:not(:disabled):not(.active) {
  background-color: #f0f0f0;
  border-color: #2980B9;
  color: #2980B9;
}

.page-btn.active {
  background-color: #2980B9;
  color: white;
  border-color: #2980B9;
  font-weight: 600;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.4;
}

/* ===== 반응형 ===== */
@media (max-width: 768px) {
  .notice-table {
    display: none;
  }

  .notice-cards {
    display: block;
    padding: 0 4px;
  }

  .search-bar {
    gap: 8px;
    padding: 0 4px;
  }

  .filter-select,
  .search-input {
    min-width: 0;
    flex-basis: calc(50% - 4px);
    font-size: 0.85rem;
    padding: 8px 10px;
  }

  .search-button {
    flex-basis: 100%;
    padding: 10px;
  }

  .last-crawled-time {
    font-size: 0.8rem;
    padding-right: 4px;
  }

  .page-btn {
    min-width: 32px;
    height: 32px;
    font-size: 0.85rem;
  }
}
</style>
