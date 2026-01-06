<template>
    <div class="table-container">
        <div class="table-card">
            <div class="table-header">
                <h2>碳酸锂各工艺成本日度变化</h2>
                <!-- <div class="table-divider"></div> -->
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>工艺路径</th>
                            <th>{{ formatDate(currentDate) }}理论成本价</th>
                            <th>{{ formatDate(prevDate) }}理论成本价</th>
                            <th>当日涨幅（%）</th>
                            <th>五日涨幅（%）</th>
                            <th>当期利润（元/吨）</th>
                            
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(item, index) in tableData" :key="index">
                            <td>{{ getProcessName(item.name) }}</td>
                            <td>{{ formatValue_floor(item.validPrice) }}</td>
                            <td>{{ formatValue_floor(item.lastvalidPrice) }}</td>
                            <td :class="getChangeClass(item.dpr)">{{ formatChange(item.dpr) }}</td>
                            <td :class="getChangeClass(item.fiveDPR)">{{ formatChange(item.fiveDPR) }}</td>
                            <td :class="getChangeClass(item.cp)">{{ formatValue_floor(item.cp) }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const currentDate = ref(''); // 当期日期（表头用）
const prevDate = ref(''); // 前日日期（表头用）
const tableData = ref([]); // 表格核心数据（数组，每个元素是一条工艺数据）

// 指标列表，后续通过map遍历这个数组，逐个获取指标数据
const indicators = [
    'compoIndex',
    'spodumene',
    'lg_Lepidolite',
    'hg_Lepidolite',
    'lfp_Cathode',
    'lfp_BattPower',
    'NCM',
    'ECSPot',
];

// 工艺名称转换，将名称映射为中文名称
const getProcessName = (name) => {
    const nameMap = {
        'compoIndex': '通惠碳酸锂综合成本指数',
        'spodumene': '锂辉石6%',
        'lg_Lepidolite': '低品位锂云母',
        'hg_Lepidolite': '高品位锂云母',
        'lfp_Cathode': '磷酸铁锂正极片',
        'lfp_BattPower': '磷酸铁锂电池粉',
        'NCM': '三元极片粉',
        'ECSPot': '电碳现货价',
        
    };
    return nameMap[name] || name; // 如果没有找到对应的中文名称，则返回原名称
};


// 日期格式化函数
const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return `${date.getMonth() + 1}月${date.getDate()}日`;
};


// 向下取整函数
const formatValue_floor = (value) => {
    if (value === null || value === undefined) return '-'; // 空值显示「-」
    return Math.floor(Number(value) * 100) / 100; // 保留两位小数并向下取整 
};



// 涨幅格式化
const formatChange = (value) => {
    if (value === null || value === undefined) return '-';
    return `${value}%`; // 转换成「X%」格式，比如 0.5 → 0.5%
};


// 样式类绑定
const getChangeClass = (value) => {
    if (value === null || value === undefined) return '';
    const numValue = parseFloat(value);
    if (numValue > 0) return 'negative'; // 正数（涨幅/利润为正）绑定 negative 类
    if (numValue < 0) return 'positive'; // 负数（涨幅/利润为负）绑定 positive 类
    return '';
};


// 数据查询异步函数
const fetchData = async () => {
    try {
        // 创建请求Promise：遍历indicators，每个指标发一个GET请求
        const promises = indicators.map(indicator =>
            axios.get('https://service.thqh.com.cn:27878/api/table', {
                params: { indicator }
            })
        );

        // 等待所有请求完成（并行请求，提升效率）
        const responses = await Promise.all(promises);

        // 处理响应：提取每个请求的data，赋值给tableData
        tableData.value = responses.map(response => response.data);

        // 提取日期
        if (tableData.value.length > 0) {
            currentDate.value = tableData.value[0].currentdate;
            prevDate.value = tableData.value[0].prevdate;
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

// 组件已完成挂载时，执行数据查询
onMounted(() => {
    fetchData();
});
</script>

<style scoped>
.table-container {
    padding: 20px;

}

.table-card {
    background: rgba(255, 255, 255, 0.5);
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
    overflow: hidden;
}

.table-header {
    padding: 20px 24px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.table-header h2 {
    margin: 0;
    color: #2c3e50;
    font-size: 30px;
    font-weight: 600;
    position: relative;
    display: inline-block;
    border-radius: 16px;
    width: 420px;
    padding: 15px 30px;
    background-image: url('@/assets/小标题背景.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center;
    z-index: 1;
}

.table-wrapper {
    padding: 20px;
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    margin-top: 10px;
}

th,
td {
    padding: 16px;
    text-align: center;
    border-bottom: 1px solid #e9ecef;
}

th {
    background: #dadada;
    color: #222222;
    font-weight: 600;
    font-size: 21px;
    white-space: nowrap;
}

td {
    color: #2c3e50;
    font-size: 18px;
}

/* 第一个指标（通惠碳酸锂综合成本指数）的特殊样式 */
tr:first-child td {
    font-size: 22px;
    font-weight: 600;
    color: #2c3e50;
    text-shadow: 
        1px 1px 0 rgba(0, 0, 0, 0.1),
        2px 2px 4px rgba(0, 0, 0, 0.1),
        0 0 10px rgba(52, 152, 219, 0.2);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(248, 249, 250, 0.1));
}

/* 保持原有的涨跌颜色样式 */
.positive {
    color: #33cc99;
    font-weight: 500;
}

.negative {
    color: #f34235;
    font-weight: 500;
}

/* 第一个指标的涨跌颜色样式覆盖 */
tr:first-child td.positive {
    color: #33cc99;
    text-shadow: 
        1px 1px 0 rgba(0, 0, 0, 0.1),
        2px 2px 4px rgba(0, 0, 0, 0.1),
        0 0 10px rgba(51, 204, 153, 0.2);
}

tr:first-child td.negative {
    color: #f34235;
    text-shadow: 
        1px 1px 0 rgba(0, 0, 0, 0.1),
        2px 2px 4px rgba(0, 0, 0, 0.1),
        0 0 10px rgba(243, 66, 53, 0.2);
}

/* 响应式布局调整 */
@media (max-width: 768px) {
    .table-container {
        padding: 10px;
    }

    .table-wrapper {
        padding: 10px;
    }

    th,
    td {
        padding: 12px 8px;
        font-size: 13px;
    }
}

@media (max-width: 480px) {
    .table-header h2 {
        font-size: 18px;
    }

    th,
    td {
        padding: 10px 6px;
        font-size: 12px;
    }
}
</style>