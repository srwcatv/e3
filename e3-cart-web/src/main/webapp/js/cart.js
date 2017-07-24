var CART = {
	itemNumChange : function(){
		$(".increment").click(function(){//＋
            console.debug($(this));
			var _thisInput = $(this).siblings("input");
			_thisInput.val(eval(_thisInput.val()) + 1);
			var x =_thisInput.closest("#danjian-0-229363");
            console.debug(x);

                var s = parseInt(x.find("input[type='text']").val())*parseInt(x.find("#pPrice").val());
                x.find("#total_price").text("¥" + s.toFixed(2));

            $.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
            var x =_thisInput.closest("#danjian-0-229363");

                var s = parseInt(x.find("input[type='text']").val())*parseInt(x.find("#pPrice").val());
                x.find("#total_price").text("¥" + s.toFixed(2));

			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
			});
		});
		/*$(".itemnum").change(function(){
			var _thisInput = $(this);
			$.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
				CART.refreshTotalPrice();
			});
		});*/
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".itemnum").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$("#allMoney2").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '¥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	}
};

$(function(){
	CART.itemNumChange();
});