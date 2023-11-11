// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function number_format(number, decimals, dec_point, thousands_sep) {
  // *     example: number_format(1234.56, 2, ',', ' ');
  // *     return: '1 234,56'
  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number,
    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
    s = '',
    toFixedFix = function(n, prec) {
      var k = Math.pow(10, prec);
      return '' + Math.round(n * k) / k;
    };
  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec);
}

var dateList = chartData.map(function(item) {
    return item.id;
});
var dailySalesTotQtyValues = chartData.map(function(item) {
    return item.dailySalesTotQty;
});
var dailySalesRevenueValues = chartData.map(function(item) {
    return item.dailySalesRevenue;
});
var dailyBoardInquiryValues = chartData.map(function(item) {
    return item.dailyBoardInquiry;
});
var dailyNewMemberValues = chartData.map(function(item) {
    return item.dailyNewMember;
});

// qtyChart
var ctx = document.getElementById("myBarChart");
var myBarChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ["2023-10-17", "2023-10-18", "2023-10-19", "2023-10-20", "2023-10-21", "2023-10-22", "2023-10-23"],
    datasets: [{
      label: "판매량",
      backgroundColor: "#4e73df",
      hoverBackgroundColor: "#2e59d9",
      borderColor: "#4e73df",
      data: [25, 30, 60, 88, 36, 55]
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'day'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 6
        },
        maxBarThickness: 25,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 100,
          maxTicksLimit: 5,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return number_format(value) + '건';
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return datasetLabel + ': ' + number_format(tooltipItem.yLabel) +'건';
        }
      }
    },
  }
});

// qtyChart
myBarChart.data.datasets[0].data = dailySalesTotQtyValues.reverse();
myBarChart.data.labels = dateList.reverse();
var parsedValues = dailySalesTotQtyValues.map(function(value) {
  return parseFloat(value);
});
var maxParsedValue = Math.max.apply(null, parsedValues);
myBarChart.options.scales.yAxes[0].ticks.max = maxParsedValue * 1.1;
myBarChart.update();


// revenueChart
var ctx = document.getElementById("myBarChart2");
var myBarChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ["2023-10-17", "2023-10-18", "2023-10-19", "2023-10-20", "2023-10-21", "2023-10-22", "2023-10-23"],
    datasets: [{
      label: "판매수익",
      backgroundColor: "#1cc88a",
      hoverBackgroundColor: "#17a673",
      borderColor: "#1cc88a",
      data: [25, 30, 60, 88, 36, 55],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'day'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 6
        },
        maxBarThickness: 25,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 100,
          maxTicksLimit: 5,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return number_format(value) + '원';
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + '원';
        }
      }
    },
  }
});
// revenueChart
myBarChart.data.datasets[0].data = dailySalesRevenueValues.reverse();
myBarChart.data.labels = dateList;
var parsedValues = dailySalesRevenueValues.map(function(value) {
  return parseFloat(value);
});
var maxParsedValue = Math.max.apply(null, parsedValues);
myBarChart.options.scales.yAxes[0].ticks.max = maxParsedValue * 1.1;
myBarChart.update();

// memberChart
var ctx = document.getElementById("myBarChart3");
var myBarChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ["2023-10-17", "2023-10-18", "2023-10-19", "2023-10-20", "2023-10-21", "2023-10-22", "2023-10-23"],
    datasets: [{
      label: "신규회원",
      backgroundColor: "#36b9cc",
      hoverBackgroundColor: "#2c9faf",
      borderColor: "#36b9cc",
      data: [25, 30, 60, 88, 36, 55],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'day'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 6
        },
        maxBarThickness: 25,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 100,
          maxTicksLimit: 5,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return number_format(value) + '명';
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + '명';
        }
      }
    },
  }
});

// memberChart
myBarChart.data.datasets[0].data = dailyNewMemberValues.reverse();
myBarChart.data.labels = dateList;
var parsedValues = dailyNewMemberValues.map(function(value) {
  return parseFloat(value);
});
var maxParsedValue = Math.max.apply(null, parsedValues);
myBarChart.options.scales.yAxes[0].ticks.max = maxParsedValue * 1.1;
myBarChart.update();

// boardChart
var ctx = document.getElementById("myBarChart4");
var myBarChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ["2023-10-17", "2023-10-18", "2023-10-19", "2023-10-20", "2023-10-21", "2023-10-22", "2023-10-23"],
    datasets: [{
      label: "게시글",
      backgroundColor: "#f6c23e",
      hoverBackgroundColor: "#f4b619",
      borderColor: "#f6c23e",
      data: [25, 30, 60, 88, 36, 55],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'day'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 6
        },
        maxBarThickness: 25,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 100,
          maxTicksLimit: 5,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return number_format(value) + '개';
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + '개';
        }
      }
    },
  }
});

// boardChart
myBarChart.data.datasets[0].data = dailyBoardInquiryValues.reverse();
myBarChart.data.labels = dateList;
var parsedValues = dailyBoardInquiryValues.map(function(value) {
  return parseFloat(value);
});
var maxParsedValue = Math.max.apply(null, parsedValues);
myBarChart.options.scales.yAxes[0].ticks.max = maxParsedValue * 1.1;
myBarChart.update();
