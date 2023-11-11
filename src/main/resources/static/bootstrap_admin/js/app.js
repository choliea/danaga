//실시간 업데이트 버튼
$(function() {
	$('#dailyUpdate').click(function() {
		$.ajax({
			url: '/admin_data/daily_update',
			method: 'GET',
			success: function(data) {
				var updatedMonthlyStat = data.updatedMonthlyStat;
				var updated7dStat = data.updated7dStat;
			},
			error: function() {
				alert('Failed to update statistic.');
			}
		});
	});
});

