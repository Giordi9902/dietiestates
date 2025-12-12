import { Routes } from '@angular/router';
import { Hero } from './pages/hero/hero';
import { Privacy } from './pages/info-pages/privacy/privacy';
import { About } from './pages/info-pages/about/about';
import { Login } from './pages/authentication-pages/login/login';
import { Register } from './pages/authentication-pages/register/register';
import { Activate } from './pages/authentication-pages/activate/activate';
import { guestGuard } from './services/guards/guest-guard';
import { Profile } from './pages/profile/profile';
import { authGuard } from './services/guards/auth-guard';
import { Search } from './pages/search/search';
import { DashboardLayout } from './pages/dashboard/dashboard-layout/dashboard-layout';
import { AgencyHomepage } from './pages/dashboard/agency-homepage/agency-homepage';
import { roleGuard } from './services/guards/role-guard';
import { AgentsManagement } from './pages/dashboard/agents-management/agents-management';
import { CreateNewAgent } from './pages/dashboard/agents-management/create-new-agent/create-new-agent';
import { ListingsManagement } from './pages/dashboard/listings-management/listings-management';

export const routes: Routes = [
  {
    path: '',
    component: Hero,
    title: 'DietiEstates25-Home',
  },
  {
    path: 'privacy',
    component: Privacy,
    title: 'Privacy',
  },
  {
    path: 'about',
    component: About,
    title: 'About Us',
  },
  {
    path: 'login',
    component: Login,
    canActivate: [guestGuard],
    title: 'Sign in',
  },
  {
    path: 'register',
    component: Register,
    canActivate: [guestGuard],
    title: 'Register',
  },
  {
    path: 'activate',
    component: Activate,
    canActivate: [guestGuard],
    title: 'Activate account',
  },
  {
    path: 'profile',
    component: Profile,
    title: 'Profile',
    canActivate: [authGuard],
  },
  {
    path: 'search',
    component: Search,
    title: 'Search listings',
  },
  {
    path: 'dashboard',
    component: DashboardLayout,
    canActivate: [authGuard],
    canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'homepage' },
      { path: 'homepage', component: AgencyHomepage, title: 'Dashboard' },
      {
        path: 'agents',
        component: AgentsManagement,
        canMatch: [roleGuard(['ROLE_ADMIN'])],
        title: 'Manage your agents',
        children: [
          {
            path: 'new',
            component: CreateNewAgent,
            canMatch: [roleGuard(['ROLE_ADMIN'])],
            title: 'Create a new agent profile',
          },
        ],
      },
      {
        path: 'listings',
        component: ListingsManagement,
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
        title: 'Manage your listings',
      },
    ],
  },
];
