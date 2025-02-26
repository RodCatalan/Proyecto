import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { MarvelComponent } from './marvel/marvel.component';
import { BitacoraComponent } from './marvel/bitacora/bitacora.component';
import { AuthenticationGuard } from './authentication/authentication.guard';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: 'marvel', component: MarvelComponent },
  { path: 'bitacora', component: BitacoraComponent , canActivate: [AuthenticationGuard]},
  { path: 'login',  component: LoginComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
