import { Grid, makeStyles } from '@material-ui/core';
import CategoryIcon from '@material-ui/icons/Category';
import {
  HomePageToolkit,
  HomePageStarredEntities,
} from '@backstage/plugin-home';
import { Content, PageWithHeader } from '@backstage/core-components';
import LibraryBooks from '@material-ui/icons/LibraryBooks';
import { HomePageSearchBar } from '@backstage/plugin-search';
import { SearchContextProvider } from '@backstage/plugin-search-react';

const useStyles = makeStyles(theme => ({
  searchBar: {
    display: 'flex',
    maxWidth: '60vw',
    backgroundColor: theme.palette.background.paper,
    boxShadow: theme.shadows[1],
    padding: '8px 0',
    borderRadius: '50px',
    margin: 'auto',
  },
  searchBarOutline: {
    borderStyle: 'none',
  },
}));

export const HomePage = () => {
  const classes = useStyles();

  return (
    <SearchContextProvider>
      <PageWithHeader title="Kadras Developer Portal" themeId="home">
        <Content>
          <Grid container justifyContent="center" spacing={6}>
            <Grid container item xs={12} alignItems="center" direction="row">
              <HomePageSearchBar
                classes={{ root: classes.searchBar }}
                InputProps={{
                  classes: { notchedOutline: classes.searchBarOutline },
                }}
                placeholder="Search"
              />
            </Grid>
            <Grid container item xs={12}>
              <Grid item xs={12} md={6}>
                <HomePageToolkit
                  title="Quick Links"
                  tools={[
                    {
                      url: '/catalog',
                      label: 'Catalog',
                      icon: <CategoryIcon />,
                    },
                    {
                      url: '/docs',
                      label: 'Tech Docs',
                      icon: <LibraryBooks />,
                    },
                  ]}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <HomePageStarredEntities />
              </Grid>
            </Grid>
          </Grid>
        </Content>
      </PageWithHeader>
    </SearchContextProvider>
  );
};
