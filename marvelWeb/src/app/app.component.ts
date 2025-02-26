import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit{

  constructor() { }

  ngOnInit(): void {

  }

  showLoading(): void {
    Swal.fire({
      title: '<h5>Cargando...</h5>',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
  }

  title = 'marvel-data-viewer';
}
