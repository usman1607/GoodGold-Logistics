function congrats() {
    $(document).ready(function () {
        let action = document.getElementById('succeed');
        if (action.innerHTML === "You have successfully registered") {
            Swal.fire({
                icon: 'success',
                title: 'Congratulations! You have successfully registered.',
                showCancelButton: false,
                focusConfirm: false,
                html:
                    'Please click on, ' +
                    '<b><a href="/login">login</a></b> ' +
                    'to log into your page',
                confirmButtonColor: '#b1ef95',
                confirmButtonText:
                    '<a href="/login"><i class="fa fa-thumbs-up"></i> Great!</a>',
                confirmButtonAriaLabel: 'Thumbs up, great!'
            })
        }
    })
}


$(document).ready(function () {
    $(".mySearch").on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $(".myTable tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});


// $(document).ready(function () {
//     $(".sellerDel").click(function (e) {
//         e.preventDefault();
//         // let href = $(this).attr("href");
//         swal.fire({
//                 title: "Are you sure?",
//                 type: "warning",
//                 showCancelButton: true,
//                 confirmButtonColor: "#DD6B55",
//                 confirmButtonText: "Yes, delete it!",
//                 cancelButtonText: "No, cancel plx!",
//                 closeOnConfirm: true,
//                 closeOnCancel: true
//             }).then((result) => {
//             if (result.isConfirmed) {
//                 window.location.href = this.href;
//                 Swal.fire(
//                     'Deleted!',
//                     'Your file has been deleted.',
//                     'success'
//                 )
//             }
//         });
//         return false;
//     });
// });

// $(document).ready(function () {
//     $("#createStaff").click(function (e) {
//         let action = document.getElementById('staff-added');
//         if (action.innerHTML === "You have successfully added a staff") {
//             Swal.fire({
//                 icon: 'success',
//                 title: 'Congrats! You have successfully added a staff.',
//                 showCancelButton: false,
//                 focusConfirm: false,
//                 confirmButtonColor: '#b1ef95',
//                 confirmButtonText:
//                     '<a href="/staff/list"><i class="fa fa-thumbs-up"></i> Great!</a>',
//                 confirmButtonAriaLabel: 'Thumbs up, great!'
//             });
//         }
//     });
// });

// $(document).ready( function () {
//     let table = $('#productsTable').DataTable({
//         "sAjaxSource": "/products/list",
//         "sAjaxDataProp": "",
//         "order": [[ 0, "asc" ]],
//         "aoColumns": [
//             { "mData": "id"},
//             { "mData": "user.lastName" },
//             { "mData": "shipment.warehouse.code" },
//             { "mData": "name" },
//             { "mData": "quantity" },
//             { "mData": "status" },
//             { "mData": "shipment.status" }
//         ]
//     })
// });

$(document).ready(function () {

    // FETCHING DATA FROM JSON FILE
    $.getJSON("/products/list",
        function (data) {
            var sno = 1;
            var product = '';

            // ITERATING THROUGH OBJECTS
            $.each(data, function (key, value) {

                //CONSTRUCTION OF ROWS HAVING
                // DATA FROM JSON OBJECT
                product += '<tr>';
                product += '<td>' +
                    sno.toString() + '</td>';

                product += '<td>' +
                    value.user.firstName + '</td>';

                product += '<td>' +
                    value.shipment.warehouse.code + '</td>';

                product += '<td>' +
                    value.name + '</td>';

                product += '<td>' +
                    value.quantity + '</td>';

                product += '<td>' +
                    value.status + '</td>';

                product += '<td>' +
                    value.shipment.status + '</td>';

                product += '<td>' +
                    '<a href="/products/details/' + value.id + '"><button class="btn btn-sm btn-info" name="button">Details</button></a>' + '</td>';

                product += '</tr>';
                sno++;
            });

            //INSERTING ROWS INTO TABLE
            $('#productsTable').append(product);
        });
});